package me.androidbox.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import me.androidbox.notes.data.NoteMarkDao
import me.androidbox.notes.data.models.NoteItemEntity

@Database(
    entities = [NoteItemEntity::class],
    version = 1)
abstract class NoteMarkDatabase : RoomDatabase() {
    abstract fun noteMarkDao(): NoteMarkDao
}
