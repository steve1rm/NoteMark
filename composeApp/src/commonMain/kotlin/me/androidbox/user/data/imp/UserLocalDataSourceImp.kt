package me.androidbox.user.data.imp

import me.androidbox.authentication.register.UserEntity
import me.androidbox.notes.data.NoteMarkDao
import me.androidbox.user.data.UserLocalDataSource
import me.androidbox.user.domain.UserRepository

class UserLocalDataSourceImp(
    private val noteMarkDao: NoteMarkDao
) : UserLocalDataSource {
    override suspend fun saveUser(userEntity: UserEntity) {

    }

    override suspend fun fetchUser(): UserEntity {

    }
}