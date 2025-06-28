package me.androidbox.notes.presentation.edit_note

sealed interface EditNoteActions {
    data class OnTitleChange(val title: String) : EditNoteActions
    data class OnContentChange(val content: String) : EditNoteActions
    data class OnLoadContent(val noteId: String) : EditNoteActions
    data class OnSaveNote (val noteId: String) : EditNoteActions
    data object OnCloseClick : EditNoteActions
    data object OnKeepEditingClick : EditNoteActions
}