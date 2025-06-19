package me.androidbox.notes.domain.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long, // Gonna make it formatted date :)
    val lastEditedAt: Long,
)
