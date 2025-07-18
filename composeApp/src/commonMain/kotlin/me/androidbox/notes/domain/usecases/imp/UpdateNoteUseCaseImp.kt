package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.UpdateNoteUseCase
import net.orandja.either.Either

class UpdateNoteUseCaseImp(private val notesRepository: NotesRepository) : UpdateNoteUseCase {
    override suspend fun execute(noteItem: NoteItem): Either<Unit, DataError> {
        return notesRepository.updateNote(noteItem)
    }
}