package me.androidbox.notes.data

import kotlinx.serialization.Serializable

@Serializable
data class GetNotesDto(
    val notes: List<NoteDto>,
    val total: Int
)
