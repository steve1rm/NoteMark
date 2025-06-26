package me.androidbox.notes.data.datasources.imp

import kotlinx.coroutines.flow.Flow
import me.androidbox.core.data.NoteMarkDatabase
import me.androidbox.core.models.DataError
import me.androidbox.notes.data.NoteMarkDao
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.models.NoteItemEntity
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right
import kotlin.coroutines.cancellation.CancellationException

class NotesLocalDataSourceImp(
    private val noteMarkDao: NoteMarkDao
) : NotesLocalDataSource {
    override suspend fun saveNote(noteItemEntity: NoteItemEntity): Either<Long, DataError.Local> {
        return try {
            val rowId = noteMarkDao.insertNote(noteItemEntity)
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
            noteMarkDao.deleteNote(noteItemEntity)
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
        return noteMarkDao.getAllNotes()
    }
}