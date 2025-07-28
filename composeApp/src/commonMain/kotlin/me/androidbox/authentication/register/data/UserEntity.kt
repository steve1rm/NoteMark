package me.androidbox.authentication.register.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.androidbox.settings.presentation.model.SyncInterval

@Entity(tableName = "UserEntity")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userName: String,
    val syncInterval: SyncInterval,
    val syncTimeStamp: Long
)