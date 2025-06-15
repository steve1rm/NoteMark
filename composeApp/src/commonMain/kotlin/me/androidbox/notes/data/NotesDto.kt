package me.androidbox.notes.data

import kotlinx.serialization.Serializable

@Serializable
data class NotesDto(
    val notes: List<NoteDto>,
    val total: Int
)
