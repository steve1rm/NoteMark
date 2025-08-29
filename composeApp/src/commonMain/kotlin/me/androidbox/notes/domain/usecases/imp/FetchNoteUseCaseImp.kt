package me.androidbox.notes.domain.usecases.imp

import me.androidbox.core.data.models.DataError
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.FetchNoteUseCase
import net.orandja.either.Either

class FetchNoteUseCaseImp(
    private val notesRepository: NotesRepository
) : FetchNoteUseCase {

    override suspend fun fetchNote(noteId: String): Either<NoteItem, DataError.Local> {
        return notesRepository.getNoteById(noteId)
    }
}