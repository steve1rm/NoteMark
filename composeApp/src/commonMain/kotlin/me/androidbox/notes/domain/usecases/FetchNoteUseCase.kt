package me.androidbox.notes.domain.usecases

import me.androidbox.core.data.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

interface FetchNoteUseCase {
    suspend fun fetchNote(noteId: String) : Either<NoteItem, DataError.Local>
}