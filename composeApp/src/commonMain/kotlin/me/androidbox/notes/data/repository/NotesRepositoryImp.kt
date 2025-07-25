package me.androidbox.notes.data.repository

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.core.domain.SyncNoteScheduler.SyncTypes.DeleteNote
import me.androidbox.core.models.DataError
import me.androidbox.core.presentation.utils.Previews.noteItem
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.data.mappers.toNoteItem
import me.androidbox.notes.data.mappers.toNoteItemDto
import me.androidbox.notes.data.mappers.toNoteItemEntity
import me.androidbox.notes.data.models.NoteMarkPendingSyncDao
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.user.data.UserLocalDataSource
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class NotesRepositoryImp(
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val notesLocalDataSource: NotesLocalDataSource,
    private val applicationScope: CoroutineScope,
    private val userLocalDataSource: UserLocalDataSource,
    private val noteMarkPendingSyncDao: NoteMarkPendingSyncDao,
    private val logoutUseCase: LogoutUseCase,
    private val syncNoteScheduler: SyncNoteScheduler,
    private val noteMarkPreferences: NoteMarkPreferences,
    private val dispatcher: Dispatchers
) : NotesRepository {
    override suspend fun saveNote(noteItem: NoteItem): Either<Unit, DataError> {
        /** Save locally to Room */
        val localResult = notesLocalDataSource.saveNote(noteItem.toNoteItemEntity())

        /** Failed to insert note into DB i.e. disk could be full,
         *  so nothing to add to the remote */
        if (localResult is Right) {
            return localResult
        }

        /**
         * We have saved the note to the local database
         * Let's protect the following code from being canceled when the user
         * navigates away from the current screen
         */
        val result = applicationScope.async {
            val networkResult = notesRemoteDataSource.createNote(noteItem.toNoteItemDto())

            when (networkResult) {
                is Left -> {
                    return@async Left(Unit)
                }

                is Right -> {
                    syncNoteScheduler.scheduleSync(SyncNoteScheduler.SyncTypes.CreateNote(
                        noteItem
                    ))

                    return@async Left(Unit)
                }
            }
        }

        return result.await()
    }

    override suspend fun updateNote(noteItem: NoteItem): Either<Unit, DataError> {
        /** save the updated note locally */
        val localResult = notesLocalDataSource.saveNote(noteItem.toNoteItemEntity())

        /** Failed to save locally, nothing to update remotely */
        if(localResult is Right) {
            return localResult
        }

        Logger.d {
            "Sucessfully saved to db"
        }

        /** Update the note remotely */
        return applicationScope.async {
            val remoteResult = notesRemoteDataSource.updateNote(noteItem.toNoteItemDto())

            when(remoteResult) {
                is Left -> {
                    Logger.d {
                        "Sucessfully saved in BE"
                    }

                    return@async Left(Unit)
                }
                is Right -> {
                    Logger.d {
                        "Error while saving in BE"
                    }
                    applicationScope.launch {
                        syncNoteScheduler.scheduleSync(
                            syncTypes = SyncNoteScheduler.SyncTypes.CreateNote(
                                noteItem = noteItem
                            )
                        )
                    }.join()
                    return@async Left(Unit)
                }
            }
        }.await()
    }

    override suspend fun deleteNote(noteItem: NoteItem): Either<Unit, DataError> {
        /** Delete it locally */
        val localResult = notesLocalDataSource.deleteNote(noteItem.toNoteItemEntity())

        if (localResult is Right) {
            return localResult
        }

        /**
         * Edge case where the note is created in offine-mode,
         * and then deleted in offline-mode as well. In that case,
         * we don't want to sync anything
         * */
        val isPendingSync = noteMarkPendingSyncDao.getNoteMarkPendingSyncEntity(noteItem.id)
        if(isPendingSync !=null) {
            noteMarkPendingSyncDao.deleteDeletedNoteMarkSyncEntity(noteItem.id)
            return Left(Unit)
        }
        
        /** Delete is remotely */
        val result = applicationScope.async {
            val remoteRemote = notesRemoteDataSource.deleteNote(noteItem.id)

            when (remoteRemote) {
                is Left -> {
                    return@async Left(Unit)
                }

                is Right -> {
                    return@async Right(remoteRemote.right)
                }
            }
        }.await()

        if(result is Right) {
            applicationScope.launch {
                syncNoteScheduler.scheduleSync(
                    syncTypes = SyncNoteScheduler.SyncTypes.DeleteNote(
                        noteId = noteItem.id
                    )
                )
            }.join()
        }

        return result
    }

    override suspend fun fetchNotes(
        page: Int,
        size: Int
    ): Flow<List<NoteItem>> {
        return notesLocalDataSource.getAllNotes().map { listOfNoteItemEntity ->
            listOfNoteItemEntity.map { noteItemEntity ->
                noteItemEntity.toNoteItem()
            }
        }
    }

    override suspend fun getNoteById(noteId: String): Either<NoteItem, DataError.Local> {
        val noteEntityResult = notesLocalDataSource.getNoteById(noteId)
        return if (noteEntityResult is Left) {
            Left(noteEntityResult.value.toNoteItem())
        } else {
            Right(noteEntityResult.right)
        }
    }

    override suspend fun nukeAllNotes(): Either<Unit, DataError.Local> {
      /*  val listOfNotesIds = notesLocalDataSource.getAllNotes()
            .map { notes ->
                notes.map { note ->
                    note.id
                }
            }.firstOrNull() ?: emptyList()*/

        notesLocalDataSource.nukeAllNotes()

        /*applicationScope.async {
            listOfNotesIds.forEach {  noteId ->
                val remoteRemote = notesRemoteDataSource.deleteNote(noteId)

                when (remoteRemote) {
                    is Right -> {
                        applicationScope.launch {
                            syncNoteScheduler.scheduleSync(
                                syncTypes = DeleteNote(
                                    noteId = noteItem.id
                                )
                            )
                        }.join()
                    }

                    is Left -> {
                        *//** no-op *//*
                    }
                }
            }
        }.await()*/

        return Left(Unit)
    }

    /** Fetches all notes from the remote data source
     *  and inserts them into the local database */
    override suspend fun fetchAllNotes(): Either<Unit, DataError> {
        val result = notesRemoteDataSource.fetchNotes(-1, 0)

        return when(result) {
            is Left -> {
                val notes = result.left.notes
                    .map { note -> note.toNoteItem() }
                    .map { noteItem -> noteItem.toNoteItemEntity() }

                applicationScope.async {
                    notesLocalDataSource.saveAllNotes(notes)
                    Left(Unit)
                }.await()
            }
            is Right -> {
                Right(result.right)
            }
        }
    }

    override suspend fun syncPendingNotes() {
        withContext(dispatcher.IO) {
            val user = when(val userResult = userLocalDataSource.fetchUser()) {
                is Left -> {
                    userResult.left
                }
                is Right -> {
                    return@withContext
                }
            }

            val createdNotes = async {
                noteMarkPendingSyncDao.getAllNoteMarkPendingSyncEntities(user.userName)
            }

            val deletedNotes = async {
                noteMarkPendingSyncDao.getAllDeletedNoteMarkSyncEntities(user.userName)
            }

            val createdNoteJobs = createdNotes
                .await()
                .map { noteMarkPendingSyncEntity ->
                    launch {
                        val noteItem = noteMarkPendingSyncEntity.noteMark.toNoteItem()

                        when(notesRemoteDataSource.createNote(noteItem.toNoteItemDto())) {
                            is Left -> {
                                applicationScope.launch {
                                    noteMarkPendingSyncDao.deleteNoteMarkPendingSyncEntity(noteItem.id)
                                }.join()
                            }
                            is Right -> {
                                Right(Unit)
                            }
                        }
                    }
                }

            val deletedNotesJob = deletedNotes
                .await()
                .map { deletedNoteMarkSyncEntity ->
                    launch {
                        when(notesRemoteDataSource.deleteNote(user.userName)) {
                            is Left -> {
                                applicationScope.launch {
                                    noteMarkPendingSyncDao.deleteNoteMarkPendingSyncEntity(user.userName)
                                }.join()
                            }
                            is Right -> {
                                Right(Unit)
                            }
                        }
                    }
                }

            createdNoteJobs.forEach { job ->
                job.join()
            }

            deletedNotesJob.forEach { job ->
                job.join()
            }
        }
    }

    /** May not use this as we have a logout usecase, so will do that in there */
    override suspend fun logout(): Either<Unit, DataError.Network> {
        val refreshToken = noteMarkPreferences.getRefreshToken()

        if(refreshToken != null) {
            logoutUseCase.execute(LogoutRequest(refreshToken = refreshToken))
        }

        TODO()
    }
}
