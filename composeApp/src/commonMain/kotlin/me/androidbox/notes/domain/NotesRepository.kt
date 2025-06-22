package me.androidbox.notes.domain

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

interface NotesRepository {
    suspend fun saveNote(noteItem: NoteItem): Either<Unit, DataError>
    suspend fun deleteNote(noteItem: NoteItem): Either<Unit, DataError>
    suspend fun fetchNotes(page: Int, size: Int): Either<List<NoteItem>, DataError.Local>
}