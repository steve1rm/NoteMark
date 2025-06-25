package me.androidbox.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.androidbox.authentication.register.UserEntity
import me.androidbox.notes.data.models.NoteItemEntity

@Dao
interface NoteMarkDao {
    @Upsert
    suspend fun insertNote(noteItemEntity: NoteItemEntity): Long

    @Delete
    suspend fun deleteNote(noteItemEntity: NoteItemEntity)

    @Query("SELECT * FROM NoteItemEntity")
    fun getAllNotes(): Flow<List<NoteItemEntity>>

    @Upsert
    suspend fun insertUser(userEntity: UserEntity): Long

    @Query("SELECT * from User LIMIT 1")
    fun getUser(): UserEntity
}