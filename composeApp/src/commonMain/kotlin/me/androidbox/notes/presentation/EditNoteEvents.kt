package me.androidbox.notes.presentation

sealed interface EditNoteEvents {
    object OnSaveNoteSuccess : EditNoteEvents
    data class OnSaveNoteFail (val message: String) : EditNoteEvents
}