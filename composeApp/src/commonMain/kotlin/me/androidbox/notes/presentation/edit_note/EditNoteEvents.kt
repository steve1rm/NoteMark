package me.androidbox.notes.presentation.edit_note

sealed interface EditNoteEvents {
    data class OnFailureMessage(val message: String) : EditNoteEvents
    object OnSaveNoteSuccess : EditNoteEvents
    object OnDiscardNote : EditNoteEvents
}