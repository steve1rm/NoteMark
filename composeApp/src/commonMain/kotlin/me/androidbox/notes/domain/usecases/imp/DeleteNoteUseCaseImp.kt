package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.data.models.DataError
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import net.orandja.either.Either

class DeleteNoteUseCaseImp(
    private val notesRepository: NotesRepository
) : DeleteNoteUseCase {
    override suspend fun execute(noteItem: NoteItem): Either<Unit, DataError> {
        return notesRepository.deleteNote(noteItem)
    }
}