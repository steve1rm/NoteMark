package me.androidbox.notes.domain

import kotlinx.coroutines.flow.Flow
import me.androidbox.core.data.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

interface NotesRepository {
    suspend fun saveNote(noteItem: NoteItem): Either<Unit, DataError>
    suspend fun updateNote(noteItem: NoteItem): Either<Unit, DataError>
    suspend fun deleteNote(noteItem: NoteItem): Either<Unit, DataError>
    fun fetchNotes(page: Int, size: Int): Flow<List<NoteItem>>
    suspend fun getNoteById(noteId: String): Either<NoteItem, DataError.Local>
    suspend fun nukeAllNotes(): Either<Unit, DataError.Local>
    suspend fun fetchAllNotes(): Either<Unit, DataError>
    suspend fun syncPendingNotes()
}