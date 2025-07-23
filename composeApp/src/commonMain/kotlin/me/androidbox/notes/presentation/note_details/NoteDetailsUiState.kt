package me.androidbox.notes.presentation.note_details

import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode

data class NoteDetailsUiState(
    val noteId: String? = null,
    val inputTitle: String = "Note title",
    val inputContent: String = "",
    val noteTitle: String = "Note title",
    val noteContent: String = "",
    val showDiscardDialog: Boolean = false,
    val noteDetailsMode: NoteDetailsMode = NoteDetailsMode.VIEWER_MODE,
    val showActionItems: Boolean = true,
    val noteCreatedDateMillis: Long? = null,
    val noteEditedDateFormatted: String = "",
    val noteCreatedDateFormatted: String = ""
)
