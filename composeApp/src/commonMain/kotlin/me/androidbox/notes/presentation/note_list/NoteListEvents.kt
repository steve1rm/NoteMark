package me.androidbox.notes.presentation.note_list

sealed interface NoteListEvents {
    data class OnNavigateToEditNote(val noteId: String) : NoteListEvents
    data class OnFailureMessage(val message: String) : NoteListEvents
}