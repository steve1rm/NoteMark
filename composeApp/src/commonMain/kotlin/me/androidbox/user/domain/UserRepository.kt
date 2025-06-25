package me.androidbox.user.domain

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun fetchUser(): User
}