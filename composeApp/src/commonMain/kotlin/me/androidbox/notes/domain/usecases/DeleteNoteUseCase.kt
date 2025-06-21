package me.androidbox.notes.domain.usecases

import me.androidbox.core.models.DataError
import net.orandja.either.Either

fun interface DeleteNoteUseCase {
    suspend fun execute(): Either<Unit, DataError.Local>
}