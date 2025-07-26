package me.androidbox.notes.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.authentication.register.domain.use_case.FetchUserByUserNameUseCaseImp
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.core.models.DataError
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.data.mappers.toNoteItem
import me.androidbox.notes.data.mappers.toNoteItemDto
import me.androidbox.notes.data.mappers.toNoteItemEntity
import me.androidbox.notes.data.models.DeletedNoteMarkSyncEntity
import me.androidbox.notes.data.models.NoteMarkPendingSyncDao
import me.androidbox.notes.data.models.NoteMarkPendingSyncEntity
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
    private val fetchUserByUserNameUseCaseImp: FetchUserByUserNameUseCaseImp,
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

        /** Adds to the sync table to be either sync'ed manually or intervals */
        val userName = fetchUserByUserNameUseCaseImp.execute()

        if(userName != null) {
            val pendingNote = NoteMarkPendingSyncEntity(
                noteMark = noteItem.toNoteItemEntity(),
                userName = userName
            )

            noteMarkPendingSyncDao.upsertNoteMarkPendingSyncEntity(pendingNote)

            return Left(Unit)
        }
        else {
            return Right(DataError.Local.EMPTY)
        }
    }

    override suspend fun updateNote(noteItem: NoteItem): Either<Unit, DataError> {
        /** save the updated note locally */
        val localResult = notesLocalDataSource.saveNote(noteItem.toNoteItemEntity())

        /** Failed to save locally, nothing to update remotely */
        if(localResult is Right) {
            return localResult
        }

        /** Adds to the sync table to be either sync'ed manually or intervals */
        val userName = fetchUserByUserNameUseCaseImp.execute()

        if(userName != null) {
            val pendingNote = NoteMarkPendingSyncEntity(
                noteMark = noteItem.toNoteItemEntity(),
                userName = userName
            )

            noteMarkPendingSyncDao.upsertNoteMarkPendingSyncEntity(pendingNote)

            return Left(Unit)
        }
        else {
            return Right(DataError.Local.EMPTY)
        }
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
        val isPendingSync =
            noteMarkPendingSyncDao.getNoteMarkPendingSyncEntity(noteItem.id)

        if(isPendingSync !=null) {
            noteMarkPendingSyncDao.deleteDeletedNoteMarkSyncEntity(noteItem.id)
            return Left(Unit)
        }

        val userId = fetchUserByUserNameUseCaseImp.execute()

        if(userId != null) {
            val deleteItem = DeletedNoteMarkSyncEntity(
                id = noteItem.id,
                userId = userId
            )
            noteMarkPendingSyncDao.upsertDeletedNoteMarkEntity(
                deletedNoteMarkSyncEntity = deleteItem
            )

            val items = noteMarkPendingSyncDao.getAllDeletedNoteMarkSyncEntities("username")
            println(items.count())

            return Left(Unit)
        }
        else {
            return Right(DataError.Local.EMPTY)
        }
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
        notesLocalDataSource.nukeAllNotes()

      /*  val listOfNotesIds = notesLocalDataSource.getAllNotes()
            .map { notes ->
                notes.map { note ->
                    note.id
                }
            }.firstOrNull() ?: emptyList()*/

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
        // QUESTION: Is it ok to have this dispatcher.IO here?
        withContext(dispatcher.IO) {
            val userId = fetchUserByUserNameUseCaseImp.execute()

            if (userId != null) {
                val createdNotes = async {
                    noteMarkPendingSyncDao.getNoteMarkPendingSyncEntitiesByUserId(userId)
                }

                val deletedNotes = async {
                    noteMarkPendingSyncDao.getAllDeletedNoteMarkSyncEntities(userId)
                }

                val createdNoteJobs = createdNotes
                    .await()
                    .map { noteMarkPendingSyncEntity ->
                        launch {
                            val noteItem = noteMarkPendingSyncEntity.noteMark.toNoteItem()

                            if(notesRemoteDataSource.createNote(noteItem.toNoteItemDto()) is Left) {

                                applicationScope.launch {
                                    notesRemoteDataSource.createNote(
                                        noteItem.toNoteItemDto()
                                    )

                                    noteMarkPendingSyncDao.deleteNoteMarkPendingSyncEntity(noteItem.id)
                                }.join()
                            }
                        }
                    }

                val deletedNotesJob = deletedNotes
                    .await()
                    .map { deletedNoteMarkSyncEntity ->
                        launch {
                            if (notesRemoteDataSource.deleteNote("eee88105-fbd1-4b6c-b031-4f1cd42ac66a") is Left) {

                                applicationScope.launch {
                                    noteMarkPendingSyncDao.deleteNoteMarkPendingSyncEntity("eee88105-fbd1-4b6c-b031-4f1cd42ac66a")
                                }.join()
                            }
                        }
                    }

                createdNoteJobs.forEach { job ->
                    job.join()
                }

                deletedNotesJob.forEach { job ->
                    job.join()
                }

                // QUESTION: Is it ok to fetch here after syncing?
                fetchAllNotes()
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
