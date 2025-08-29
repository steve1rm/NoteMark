package me.androidbox.notes.domain.usecases

import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

fun interface FetchAllNotesUseCase {
    suspend fun execute(): Either<Unit, DataError>
}