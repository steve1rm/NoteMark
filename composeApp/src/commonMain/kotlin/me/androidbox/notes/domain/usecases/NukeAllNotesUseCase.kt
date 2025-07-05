package me.androidbox.notes.domain.usecases

import me.androidbox.core.models.DataError
import net.orandja.either.Either

fun interface NukeAllNotesUseCase {
    suspend fun execute(): Either<Unit, DataError.Local>
}