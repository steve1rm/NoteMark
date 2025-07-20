package me.androidbox.notes.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoteMarkPendingSyncEntity")
data class NoteMarkPendingSyncEntity(
    @Embedded val noteMark: NoteItemEntity,
    @PrimaryKey(autoGenerate = false)
    val noteMarkId: String = noteMark.id,
    val userName: String
)
