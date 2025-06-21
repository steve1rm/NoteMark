package me.androidbox.notes.domain.model

data class NoteItem(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long, // Gonna make it formatted date :), let's think about this first
    val lastEditedAt: Long,
)
