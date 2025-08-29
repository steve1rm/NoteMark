package me.androidbox.notes.domain.usecases

import me.androidbox.core.data.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

fun interface UpdateNoteUseCase {
    suspend fun execute(noteItem: NoteItem): Either<Unit, DataError>
}