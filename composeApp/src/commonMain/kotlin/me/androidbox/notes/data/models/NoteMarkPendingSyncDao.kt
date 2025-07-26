package me.androidbox.notes.data.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteMarkPendingSyncDao {

    /** Creating Note Mark */
  /*  @Query("SELECT * FROM NoteMarkPendingSyncEntity WHERE noteMarkId=:id")
    suspend fun getAllNoteMarkPendingSyncEntities(id: String): List<NoteMarkPendingSyncEntity>
*/
    @Query("SELECT * FROM NoteMarkPendingSyncEntity WHERE userName=:userName")
    suspend fun getNoteMarkPendingSyncEntity(userName: String): NoteMarkPendingSyncEntity?

    @Query("SELECT * FROM NoteMarkPendingSyncEntity WHERE userName=:userName")
    suspend fun getNoteMarkPendingSyncEntitiesByUserId(userName: String): List<NoteMarkPendingSyncEntity>


    @Upsert
    suspend fun upsertNoteMarkPendingSyncEntity(noteMarkPendingSyncEntity: NoteMarkPendingSyncEntity)

    @Query("DELETE FROM NoteMarkPendingSyncEntity WHERE id=:noteId")
    suspend fun deleteNoteMarkPendingSyncEntity(noteId: String)

    /** Deleting Note Mark */

    @Query("SELECT * FROM DeletedNoteMarkSyncEntity WHERE userId=:userId")
    suspend fun getAllDeletedNoteMarkSyncEntities(userId: String): List<DeletedNoteMarkSyncEntity>

    @Upsert
    suspend fun upsertDeletedNoteMarkEntity(deletedNoteMarkSyncEntity: DeletedNoteMarkSyncEntity)

    @Query("DELETE FROM DeletedNoteMarkSyncEntity WHERE id=:id")
    suspend fun deleteDeletedNoteMarkSyncEntity(id: String)
}