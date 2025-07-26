package me.androidbox.notes.presentation.note_details

sealed interface NoteDetailsEvents {
    data class OnFailureMessage(val message: String) : NoteDetailsEvents
    object OnSaveNoteDetailsSuccess : NoteDetailsEvents
    object OnDeleteNoteSuccess : NoteDetailsEvents
    object OnQuitScreen : NoteDetailsEvents
}