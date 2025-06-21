package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.FetchNotesUseCase
import net.orandja.either.Either

class FetchNotesUseCaseImp : FetchNotesUseCase {
    override suspend fun execute(): Either<List<NoteItem>, DataError.Local> {
        TODO("Not yet implemented")
    }
}