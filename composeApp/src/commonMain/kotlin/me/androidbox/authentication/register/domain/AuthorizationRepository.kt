package me.androidbox.authentication.register.domain

import me.androidbox.authentication.login.domain.model.LoginRequest
import me.androidbox.authentication.login.domain.model.LoginResponse
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.register.data.RegisterDto
import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface AuthorizationRepository {
    suspend fun register(registerDto: RegisterDto) : Either<Unit, DataError>
    suspend fun login(loginRequest: LoginRequest) : Either<Unit, DataError.Network>
    suspend fun loginV2(loginRequest: LoginRequest) : Either<LoginResponse, DataError.Network>
    suspend fun logout(logoutRequest: LogoutRequest) : Either<Unit, DataError.Network>

}