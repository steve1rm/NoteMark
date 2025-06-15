package me.androidbox.authentication.register.data

import me.androidbox.authentication.login.data.LoginDto
import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface AuthorizationRemoteDataSource {
    suspend fun registerUser(register: Register) : Either<Unit, DataError>
    suspend fun loginUser(loginDto: LoginDto) : Either<Unit, DataError.Network>
}