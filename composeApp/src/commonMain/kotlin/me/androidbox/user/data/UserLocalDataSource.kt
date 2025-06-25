package me.androidbox.user.data

import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.core.models.DataError
import me.androidbox.user.domain.User
import net.orandja.either.Either

interface UserLocalDataSource {
    suspend fun saveUser(userEntity: UserEntity) : Either<Long, DataError.Local>
    suspend fun fetchUser() : Either<UserEntity?, DataError.Local>
}