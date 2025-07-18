package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.usecases.FetchAllNotesUseCase
import net.orandja.either.Either

class FetchAllNotesUseCaseImp(
    private val notesRepository: NotesRepository
) : FetchAllNotesUseCase {
    override suspend fun execute(): Either<Unit, DataError> {
        return notesRepository.fetchAllNotes()
    }
}