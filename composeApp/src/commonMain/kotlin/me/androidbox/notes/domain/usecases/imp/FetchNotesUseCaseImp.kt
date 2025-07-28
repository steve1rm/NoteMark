package me.androidbox.notes.domain.usecases.imp

import kotlinx.coroutines.flow.Flow
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.FetchNotesUseCase

class FetchNotesUseCaseImp(
    private val notesRepository: NotesRepository
) : FetchNotesUseCase {
    override suspend fun execute(): Flow<List<NoteItem>> {
        return notesRepository.fetchNotes(-1, 0)
    }
}