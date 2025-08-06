package me.androidbox.notes.domain.model

data class NoteItem(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long, // Gonna make it formatted date :), let's think about this first
    val lastEditedAt: Long,
)

// FEEDBACK: Date formatting -> presentation-layer concern
// It can make sense to work with ZonedDateTime or Instant here instead - provides more utility
// than a Long.
