package me.androidbox.authentication.register.domain

import me.androidbox.authentication.login.data.Login
import me.androidbox.authentication.register.data.Register
import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface AuthorizationRepository {
    suspend fun register(register: Register) : Either<Unit, DataError>
    suspend fun login(login: Login) : Either<Unit, DataError>
}