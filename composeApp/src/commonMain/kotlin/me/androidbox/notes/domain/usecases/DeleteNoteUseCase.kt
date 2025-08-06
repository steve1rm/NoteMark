package me.androidbox.notes.domain.usecases

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

// FEEDBACK: What value does this abstraction add?
// Downsides:
// 1. Bloats the project
// 2. Makes it harder to navigate through layers
//
// Upsides: ???
fun interface DeleteNoteUseCase {
    suspend fun execute(noteItem: NoteItem): Either<Unit, DataError>
}