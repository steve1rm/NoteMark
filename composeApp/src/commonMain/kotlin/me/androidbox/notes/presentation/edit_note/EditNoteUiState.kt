package me.androidbox.notes.presentation.edit_note

data class EditNoteUiState(
    val inputTitle: String = "Note title",
    val inputContent: String = "",
    val noteTitle: String = "",
    val noteContent: String = "",
    val showDiscardDialog : Boolean = false
)
