package me.androidbox.notes.presentation.note_list

import me.androidbox.notes.domain.model.NoteItem

sealed interface NoteListActions {
    data class OnShowDeleteDialog(val noteItem: NoteItem) : NoteListActions
    data class OnDeleteNote(val noteItem: NoteItem) : NoteListActions
    object OnCancelDeleteDialog : NoteListActions
    object OnNavigateToEditNoteWithNewNote : NoteListActions
    data class OnNavigateToEditNoteWithNoteId(val noteId: String) : NoteListActions
}