package me.androidbox.notes.presentation

import me.androidbox.notes.domain.model.NoteItem

sealed interface EditNoteActions {
    data class OnTitleChange(val title: String) : EditNoteActions
    data class OnContentChange(val content: String) : EditNoteActions
    data class OnSaveNote(val noteItem: NoteItem) : EditNoteActions
}