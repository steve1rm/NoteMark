package me.androidbox.notes.data.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteMarkPendingSyncDao {

    /** Creating Note Mark */
    @Query("SELECT * FROM NoteMarkPendingSyncEntity WHERE noteMarkId=:id")
    suspend fun getAllNoteMarkPendingSyncEntities(id: String): List<NoteMarkPendingSyncEntity>

    @Query("SELECT * FROM NoteMarkPendingSyncEntity WHERE userName=:userName")
    suspend fun getNoteMarkPendingSyncEntity(userName: String): NoteMarkPendingSyncEntity?

    @Upsert
    suspend fun upsertNoteMarkPendingSyncEntity(noteMarkPendingSyncEntity: NoteMarkPendingSyncEntity)

    @Query("DELETE FROM NoteMarkPendingSyncEntity WHERE noteMarkId=:id")
    suspend fun deleteNoteMarkPendingSyncEntity(id: String)

    /** Deleting Note Mark */

    @Query("SELECT * FROM DeletedNoteMarkSyncEntity WHERE userName=:userName")
    suspend fun getAllDeletedNoteMarkSyncEntities(userName: String): List<DeletedNoteMarkSyncEntity>

    @Upsert
    suspend fun upsertDeletedNoteMarkEntity(deletedNoteMarkSyncEntity: DeletedNoteMarkSyncEntity)

    @Query("DELETE FROM DeletedNoteMarkSyncEntity WHERE id=:id")
    suspend fun deleteDeletedNoteMarkSyncEntity(id: String)
}