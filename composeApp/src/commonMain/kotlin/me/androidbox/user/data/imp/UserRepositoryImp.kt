package me.androidbox.user.data.imp

import me.androidbox.user.data.UserLocalDataSource
import me.androidbox.user.data.toUser
import me.androidbox.user.data.toUserEntity
import me.androidbox.user.domain.User
import me.androidbox.user.domain.UserRepository

class UserRepositoryImp(
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun saveUser(user: User) {
        userLocalDataSource.saveUser(user.toUserEntity())
    }

    override suspend fun fetchUser(): User {
        return userLocalDataSource.fetchUser().toUser()
    }
}