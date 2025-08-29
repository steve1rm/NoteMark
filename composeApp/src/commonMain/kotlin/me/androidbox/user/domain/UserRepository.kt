package me.androidbox.user.domain

import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun fetchUser(userName: String): Either<User, DataError.Local>
}