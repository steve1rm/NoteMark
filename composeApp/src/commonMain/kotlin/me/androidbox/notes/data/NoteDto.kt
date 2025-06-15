package me.androidbox.notes.data

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val lastEditedAt: Long
)
