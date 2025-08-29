package me.androidbox.authentication.register.data

import me.androidbox.authentication.login.data.LoginRequestDto
import me.androidbox.authentication.login.data.LoginResponseDto
import me.androidbox.authentication.login.data.LogoutRequestDto
import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

interface AuthorizationRemoteDataSource {
    suspend fun registerUser(registerDto: RegisterDto) : Either<Unit, DataError>
    suspend fun loginUser(loginRequestDto: LoginRequestDto) : Either<Unit, DataError.Network>
    suspend fun loginUserV2(loginRequestDto: LoginRequestDto) : Either<LoginResponseDto, DataError.Network>
    suspend fun logoutUser(logoutRequestDto: LogoutRequestDto) : Either<Unit, DataError.Network>
}