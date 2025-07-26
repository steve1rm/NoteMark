package me.androidbox.notes.presentation.note_list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.androidbox.notes.domain.model.NoteItem

data class NoteListUiState(
    val profilePicText: String = "",
    val notesList: ImmutableList<NoteItem> = persistentListOf(),
    val showDeleteDialog: Boolean = false,
    val currentSelectedNote: NoteItem? = null,
    val isConnected: Boolean = false
)
