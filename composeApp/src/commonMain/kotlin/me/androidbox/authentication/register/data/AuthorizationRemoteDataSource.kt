package me.androidbox.authentication.register.data

import me.androidbox.authentication.login.data.Login
import me.androidbox.authentication.login.data.TokenDto
import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface AuthorizationRemoteDataSource {
    suspend fun registerUser(register: Register) : Either<Unit, DataError>
    suspend fun loginUser(login: Login) : Either<TokenDto, DataError>
}