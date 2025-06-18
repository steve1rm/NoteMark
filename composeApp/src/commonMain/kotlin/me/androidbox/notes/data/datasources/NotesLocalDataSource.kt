package me.androidbox.notes.data.datasources

import kotlinx.coroutines.flow.Flow
import me.androidbox.core.models.DataError
import me.androidbox.notes.data.models.NoteItemEntity
import net.orandja.either.Either

interface NotesLocalDataSource {
    suspend fun saveNote(noteItemEntity: NoteItemEntity): Either<Long, DataError.Local>
    suspend fun updateNote(noteItemEntity: NoteItemEntity): Either<Long, DataError.Local>
    fun getAllNotes(): Flow<List<NoteItemEntity>>
}
