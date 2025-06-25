package me.androidbox.authentication.register

import androidx.room.Entity

@Entity(tableName = "User")
data class UserEntity(
    val userName: String,
    val email: String
)
