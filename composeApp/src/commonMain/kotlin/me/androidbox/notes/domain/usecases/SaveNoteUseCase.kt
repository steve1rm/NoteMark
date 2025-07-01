package me.androidbox.notes.domain.usecases

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

fun interface SaveNoteUseCase {
    suspend fun execute(noteItem: NoteItem): Either<Unit, DataError>
}