package me.androidbox.notes.data.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteItemDto(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String
)