package me.androidbox.notes.presentation

import me.androidbox.core.presentation.utils.Previews
import me.androidbox.notes.domain.model.NoteItem

data class NoteListUiState(
    val profilePicText: String = "PL",
    val notesList: List<NoteItem> = emptyList(),
    val showDeleteDialog: Boolean = false,
    val currentSelectedNote: NoteItem? = null
)
