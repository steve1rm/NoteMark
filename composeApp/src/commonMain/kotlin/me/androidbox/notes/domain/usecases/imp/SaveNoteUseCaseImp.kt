package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import net.orandja.either.Either

class SaveNoteUseCaseImp(private val notesRepository: NotesRepository) : SaveNoteUseCase {
    override suspend fun execute(noteItem: NoteItem): Either<Long, DataError.Local> {
        return notesRepository.saveNote(noteItem)
    }
}