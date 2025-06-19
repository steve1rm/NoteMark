package me.androidbox.notes.presentation

import me.androidbox.core.presentation.utils.Previews
import me.androidbox.notes.domain.model.Note

data class NoteListUiState(
    val profilePicText: String = "PL",
    val notesList: List<Note> = Previews.noteList
)
