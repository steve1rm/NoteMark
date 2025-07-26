package me.androidbox.notes.presentation.note_details

import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode

sealed interface NoteDetailsActions {
    data class OnTitleChange(val title: String) : NoteDetailsActions
    data class OnLoadContent(val noteId: String?) : NoteDetailsActions
    data class OnContentChange(val content: String) : NoteDetailsActions
    data object OnCloseClick : NoteDetailsActions
    data object OnReaderScreenClick: NoteDetailsActions
    data class OnModeChange(val mode: NoteDetailsMode) : NoteDetailsActions
    data object OnBackPressed : NoteDetailsActions
}