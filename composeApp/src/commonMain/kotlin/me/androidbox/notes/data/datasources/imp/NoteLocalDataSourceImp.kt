package me.androidbox.notes.data.datasources.imp

import kotlinx.coroutines.flow.Flow
import me.androidbox.core.data.NoteMarkDatabase
import me.androidbox.core.models.DataError
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.models.NoteItemEntity
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right
import kotlin.coroutines.cancellation.CancellationException

class NoteLocalDataSourceImp(
    private val notesDatabase: NoteMarkDatabase
) : NotesLocalDataSource {
    override suspend fun saveNote(noteItemEntity: NoteItemEntity): Either<Long, DataError.Local> {
        return try {
            val rowId = notesDatabase.noteMarkDao().insertNote(noteItemEntity)
            Left(rowId)
        }
        catch(exception: Exception) {
            if(exception is CancellationException) {
                throw exception
            }
            Right(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteNote(noteItemEntity: NoteItemEntity): Either<Unit, DataError.Local> {
        return try {
            notesDatabase.noteMarkDao().deleteNote(noteItemEntity)
            Left(Unit)
        }
        catch(exception: Exception) {
            if(exception is CancellationException) {
                throw exception
            }
            Right(DataError.Local.DISK_FULL)
        }
    }

    override fun getAllNotes(): Flow<List<NoteItemEntity>> {
        return notesDatabase.noteMarkDao().getAllNotes()
    }
}