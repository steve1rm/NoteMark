package me.androidbox.notes.presentation.note_list

import me.androidbox.notes.domain.model.NoteItem

data class NoteListUiState(
    val profilePicText: String = "",
    val notesList: List<NoteItem> = emptyList(),
    val showDeleteDialog: Boolean = false,
    val currentSelectedNote: NoteItem? = null
)
