package me.androidbox.notes.data.datasources.imp

import kotlinx.coroutines.flow.Flow
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
        } catch (exception: Exception) {
            if (exception is CancellationException) {
                throw exception
            }
            Right(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteNote(noteItemEntity: NoteItemEntity): Either<Unit, DataError.Local> {
        return try {
            println("Attempting to delete note: ${noteItemEntity.id}")

            // Check if the note exists before deletion
            val existingNote = noteMarkDao.getNoteById(noteItemEntity.id)
            println("Existing note found: $existingNote")

            if (existingNote != null) {
                noteMarkDao.deleteNote(noteItemEntity)
                println("Delete operation completed")

                // Verify deletion
                val verifyDelete = noteMarkDao.getNoteById(noteItemEntity.id)
                println("Note after deletion: $verifyDelete")
            } else {
                println("Note not found in database")
            }
            Left(Unit)
        } catch (exception: Exception) {
            if (exception is CancellationException) {
                throw exception
            }
            println("Delete error: ${exception.message}")
            exception.printStackTrace()
            Right(DataError.Local.UNKNOWN)
        }
    }

    override fun getAllNotes(): Flow<List<NoteItemEntity>> {
        return noteMarkDao.getAllNotes()
    }

    override suspend fun getNoteById(noteId: String): Either<NoteItemEntity, DataError.Local> {
        val note = noteMarkDao.getNoteById(noteId = noteId)
        return if (note != null) {
            Left(note)
        } else {
            Right(DataError.Local.EMPTY)
        }
    }

    override suspend fun nukeAllNotes(): Either<Unit, DataError.Local> {
        return try {
            noteMarkDao.nukeAllNotes()
            Left(Unit)
        }
        catch (exception: Exception) {
            if(exception is CancellationException) {
                throw exception
            }
            Right(DataError.Local.UNKNOWN)
        }
    }
}