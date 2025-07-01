package me.androidbox.notes.data.datasources

import kotlinx.coroutines.flow.Flow
import me.androidbox.core.models.DataError
import me.androidbox.notes.data.models.NoteItemEntity
import net.orandja.either.Either

interface NotesLocalDataSource {
    suspend fun saveNote(noteItemEntity: NoteItemEntity): Either<Long, DataError.Local>
    suspend fun deleteNote(noteItemEntity: NoteItemEntity): Either<Unit, DataError.Local>
    fun getAllNotes(): Flow<List<NoteItemEntity>>
    suspend fun getNoteById(noteId: String): Either<NoteItemEntity, DataError.Local>
}
