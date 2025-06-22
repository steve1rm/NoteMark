package me.androidbox.notes.domain

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

interface NotesRepository {
    suspend fun saveNote(noteItem: NoteItem): Either<Unit, DataError>
    suspend fun deleteNote(id: String): Either<Unit, DataError.Local>
    suspend fun fetchNotes(page: Int, size: Int): Either<List<NoteItem>, DataError.Local>
}