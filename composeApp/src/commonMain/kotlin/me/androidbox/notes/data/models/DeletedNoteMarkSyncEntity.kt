package me.androidbox.notes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "DeletedNoteMarkSyncEntity")
data class DeletedNoteMarkSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val userName: String
)
