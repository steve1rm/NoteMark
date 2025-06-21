package me.androidbox.notes.data.models

import kotlinx.serialization.Serializable

@Serializable
data class NotesDto(
    val notes: List<NoteItemDto>,
    val total: Int
)