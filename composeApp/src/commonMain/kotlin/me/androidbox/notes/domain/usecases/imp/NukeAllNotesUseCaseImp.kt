package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.data.models.DataError
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.usecases.NukeAllNotesUseCase
import net.orandja.either.Either

class NukeAllNotesUseCaseImp(
    private val notesRepository: NotesRepository
) : NukeAllNotesUseCase {
    override suspend fun execute(): Either<Unit, DataError.Local> {
        return notesRepository.nukeAllNotes()
    }
}