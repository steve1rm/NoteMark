package me.androidbox.authentication.register.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserEntity")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val userName: String,
    val email: String
)