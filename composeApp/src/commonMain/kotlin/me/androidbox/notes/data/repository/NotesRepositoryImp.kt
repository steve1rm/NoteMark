package me.androidbox.notes.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.androidbox.authentication.register.domain.use_case.FetchUserByUserNameUseCaseImp
import me.androidbox.core.data.models.DataError
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
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class NotesRepositoryImp(
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val notesLocalDataSource: NotesLocalDataSource,
    private val applicationScope: CoroutineScope,
    private val noteMarkPendingSyncDao: NoteMarkPendingSyncDao,
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

        /**
         * We have saved the note to the local database
         * Let's protect the following code from being canceled when the user
         * navigates away from the current screen and save remotely
         */
        val result = applicationScope.async {
            val networkResult = notesRemoteDataSource.createNote(noteItem.toNoteItemDto())

            when (networkResult) {
                is Left -> {
                    Left(Unit)
                }

                is Right -> {
                    /** Adds to the sync table to be either sync'ed manually or intervals */
                    val userName = fetchUserByUserNameUseCaseImp.execute()

                    if(userName != null) {
                        val pendingNote = NoteMarkPendingSyncEntity(
                            noteMark = noteItem.toNoteItemEntity(),
                            userName = userName
                        )

                        noteMarkPendingSyncDao.upsertNoteMarkPendingSyncEntity(pendingNote)
                        Left(Unit)
                    }
                    else {
                        Right(DataError.Local.EMPTY)
                    }
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

        /**
         * We have saved the note to the local database
         * Let's protect the following code from being canceled when the user
         * navigates away from the current screen and save remotely
         */
        val result = applicationScope.async {
            val networkResult = notesRemoteDataSource.updateNote(noteItem.toNoteItemDto())

            when (networkResult) {
                is Left -> {
                    Left(Unit)
                }

                is Right -> {
                    /** Adds to the sync table to be either sync'ed manually or intervals */
                    val userName = fetchUserByUserNameUseCaseImp.execute()

                    if(userName != null) {
                        val pendingNote = NoteMarkPendingSyncEntity(
                            noteMark = noteItem.toNoteItemEntity(),
                            userName = userName
                        )

                        noteMarkPendingSyncDao.upsertNoteMarkPendingSyncEntity(pendingNote)
                        Left(Unit)
                    }
                    else {
                        Right(DataError.Local.EMPTY)
                    }
                }
            }
        }

        return result.await()
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

        if(isPendingSync != null) {
            noteMarkPendingSyncDao.deleteDeletedNoteMarkSyncEntity(noteItem.id)
            return Left(Unit)
        }

        /**
         * We have saved the note to the local database
         * Let's protect the following code from being canceled when the user
         * navigates away from the current screen and save remotely
         */
        val result = applicationScope.async {
            val networkResult = notesRemoteDataSource.deleteNote(noteItem.id)

            when (networkResult) {
                is Left -> {
                    Left(Unit)
                }

                is Right -> {
                    /** Adds to the sync table to be either sync'ed manually or intervals */
                    val userName = fetchUserByUserNameUseCaseImp.execute()

                    if(userName != null) {
                        val deleteItem = DeletedNoteMarkSyncEntity(
                            id = noteItem.id,
                            userId = userName
                        )
                        noteMarkPendingSyncDao.upsertDeletedNoteMarkEntity(
                            deletedNoteMarkSyncEntity = deleteItem
                        )

                        Left(Unit)
                    }
                    else {
                        Right(DataError.Local.EMPTY)
                    }
                }
            }
        }

        return result.await()
    }

    override fun fetchNotes(
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
        return notesLocalDataSource.nukeAllNotes()
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
                    // necessary to delete notes that have been deleted server-side only (e.g. from a different device).
                    notesLocalDataSource.nukeAllNotes()
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

        // FEEDBACK: Answer: It's not necessary, since you just call suspending functions
        withContext(dispatcher.IO) {
            val userName = fetchUserByUserNameUseCaseImp.execute()

            if (userName != null) {
                val createdNotes = async {
                    noteMarkPendingSyncDao.getNoteMarkPendingSyncEntitiesByUserId(userName)
                }

                val deletedNotes = async {
                    noteMarkPendingSyncDao.getAllDeletedNoteMarkSyncEntities(userName)
                }

                val createdNoteJobs = createdNotes
                    .await()
                    .map { noteMarkPendingSyncEntity ->
                        launch {
                            val noteItem = noteMarkPendingSyncEntity.noteMark.toNoteItem()

                            // FEEDBACK: Notes being pushed to remote twice here
                            if(notesRemoteDataSource.createNote(noteItem.toNoteItemDto()) is Left) {
                                applicationScope.launch {
                                    notesRemoteDataSource.createNote(
                                        noteItem.toNoteItemDto()
                                    )

                                    // QUESTION Is it ok to delete right after send not to remote

                                    // FEEDBACK: Answer, yes but only after having checked the result
                                    noteMarkPendingSyncDao.deleteNoteMarkPendingSyncEntity(noteItem.id)
                                }.join()
                            }
                        }
                    }

                val deletedNotesJob = deletedNotes
                    .await()
                    .map { deletedNoteMarkSyncEntity ->
                        launch {
                            applicationScope.launch {
                                // Delete remotely
                                notesRemoteDataSource.deleteNote(deletedNoteMarkSyncEntity.id)
                                // Delete locally
                                noteMarkPendingSyncDao.deleteNoteMarkPendingSyncEntity(deletedNoteMarkSyncEntity.id)
                            }.join()
                        }
                    }

                createdNoteJobs.forEach { job ->
                    job.join()
                }

                deletedNotesJob.forEach { job ->
                    job.join()
                }

                // QUESTION: Is it ok to fetch here after syncing?
                // Will it still suspend if the user clicks back button?

                // FEEDBACK: Answer: Whether it suspends depends on the coroutine scope this
                // suspend function is called in. If it's viewModelScope, it will be cancelled
                // when user goes back.
                fetchAllNotes()
            }
        }
    }
}
