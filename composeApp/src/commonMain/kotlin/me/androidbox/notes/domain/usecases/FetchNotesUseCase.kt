package me.androidbox.notes.domain.usecases

import kotlinx.coroutines.flow.Flow
import me.androidbox.notes.domain.model.NoteItem

fun interface FetchNotesUseCase {
    fun execute(): Flow<List<NoteItem>>
}