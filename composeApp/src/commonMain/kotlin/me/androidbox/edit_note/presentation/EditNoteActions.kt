package me.androidbox.edit_note.presentation

sealed interface EditNoteActions {
    data class OnTitleChange(val title: String) : EditNoteActions
    data class OnContentChange(val content: String) : EditNoteActions
}