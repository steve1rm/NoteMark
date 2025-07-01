package me.androidbox.user.domain

import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun fetchUser(): Either<User, DataError.Local>
}