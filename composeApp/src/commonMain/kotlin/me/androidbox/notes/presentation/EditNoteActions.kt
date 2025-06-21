package me.androidbox.notes.presentation

sealed interface EditNoteActions {
    data class OnTitleChange(val title: String) : EditNoteActions
    data class OnContentChange(val content: String) : EditNoteActions
    data object OnSaveNote : EditNoteActions
}