package me.androidbox.user.data

import me.androidbox.authentication.register.UserEntity

interface UserLocalDataSource {
    suspend fun saveUser(userEntity: UserEntity)
    suspend fun fetchUser() : UserEntity
}