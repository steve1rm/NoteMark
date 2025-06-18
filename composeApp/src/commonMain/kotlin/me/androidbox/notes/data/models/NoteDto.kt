package me.androidbox.notes.data.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val lastEditedAt: Long
)