package me.androidbox.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.notes.data.NoteMarkDao
import me.androidbox.notes.data.models.DeletedNoteMarkSyncEntity
import me.androidbox.notes.data.models.NoteItemEntity
import me.androidbox.notes.data.models.NoteMarkPendingSyncDao
import me.androidbox.notes.data.models.NoteMarkPendingSyncEntity

@Database(
    entities = [
        NoteItemEntity::class,
        UserEntity::class,
        DeletedNoteMarkSyncEntity::class,
        NoteMarkPendingSyncEntity::class],
    version = 1
)
abstract class NoteMarkDatabase : RoomDatabase() {
    abstract fun noteMarkDao(): NoteMarkDao

    abstract fun noteMarkPendingSyncDao(): NoteMarkPendingSyncDao
}
