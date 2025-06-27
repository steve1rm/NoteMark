package me.androidbox.notes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoteItemEntity")
data class NoteItemEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long, // Gonna make it formatted date :), let's think about this first
    val lastEditedAt: Long,
)