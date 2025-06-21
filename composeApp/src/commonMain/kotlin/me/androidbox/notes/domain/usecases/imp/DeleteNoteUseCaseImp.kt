package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import net.orandja.either.Either

class DeleteNoteUseCaseImp : DeleteNoteUseCase {
    override suspend fun execute(): Either<Unit, DataError.Local> {
        TODO("Not yet implemented")
    }
}