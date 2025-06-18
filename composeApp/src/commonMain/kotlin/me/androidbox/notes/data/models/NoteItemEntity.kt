package me.androidbox.notes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String
)