package me.androidbox.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.notes.data.models.NoteItemEntity

@Dao
interface NoteMarkDao {
    /** Notes */
    @Upsert
    suspend fun insertNote(noteItemEntity: NoteItemEntity): Long
    @Upsert
    suspend fun insertAllNotes(noteItemsEntity: List<NoteItemEntity>): List<Long>

    @Delete
    suspend fun deleteNote(noteItemEntity: NoteItemEntity)

    @Query("DELETE FROM NoteItemEntity")
    suspend fun nukeAllNotes()

    @Query("SELECT * FROM NoteItemEntity")
    fun getAllNotes(): Flow<List<NoteItemEntity>>

    /** User */
    @Upsert
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("SELECT * from UserEntity WHERE userName = :userName LIMIT 1")
    suspend fun getUser(userName: String): UserEntity?

    @Query("DELETE FROM UserEntity")
    suspend fun nukeAllUsers()
    
    @Query("SELECT * from NoteItemEntity WHERE id = :noteId LIMIT 1")
    suspend fun getNoteById(noteId: String): NoteItemEntity?
}