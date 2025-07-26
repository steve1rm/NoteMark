package me.androidbox.user.data

import me.androidbox.authentication.register.data.UserEntity
import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface UserLocalDataSource {
    suspend fun saveUser(userEntity: UserEntity) : Either<Long, DataError.Local>
    suspend fun fetchUserByUserName(userName: String) : Either<UserEntity, DataError.Local>
}