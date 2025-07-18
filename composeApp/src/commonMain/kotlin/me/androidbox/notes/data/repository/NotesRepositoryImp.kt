package me.androidbox.notes.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.androidbox.core.models.DataError
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.data.mappers.toNoteItem
import me.androidbox.notes.data.mappers.toNoteItemDto
import me.androidbox.notes.data.mappers.toNoteItemEntity
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class NotesRepositoryImp(
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val notesLocalDataSource: NotesLocalDataSource,
    private val applicationScope: CoroutineScope
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
                    return@async networkResult
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

        /** Update the note remotely */
        return applicationScope.async {
            val remoteResult = notesRemoteDataSource.updateNote(noteItem.toNoteItemDto())

            when(remoteResult) {
                is Left -> {
                    return@async Left(Unit)
                }
                is Right -> {
                    return@async Right(remoteResult.right)
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

        /** Delete is remotely */
        val result = applicationScope.async {
            val remoteRemote = notesRemoteDataSource.deleteNote(noteItem.id)

            when (remoteRemote) {
                is Left -> {
                    return@async Left(Unit)
                }

                is Right -> {
                    remoteRemote
                }
            }
        }

        return result.await()
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
                    notesLocalDataSource.saveAllNotes(notes)
                    Left(Unit)
                }.await()
            }
            is Right -> {
                Right(result.right)
            }
        }
    }
}