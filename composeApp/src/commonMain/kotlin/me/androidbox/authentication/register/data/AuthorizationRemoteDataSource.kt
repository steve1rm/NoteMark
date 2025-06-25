package me.androidbox.authentication.register.data

import me.androidbox.authentication.login.data.LoginDto
import me.androidbox.authentication.login.data.LoginV2Dto
import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface AuthorizationRemoteDataSource {
    suspend fun registerUser(registerDto: RegisterDto) : Either<Unit, DataError>
    suspend fun loginUser(loginDto: LoginDto) : Either<Unit, DataError.Network>
    suspend fun loginUserV2(loginV2Dto: LoginV2Dto) : Either<Unit, DataError.Network>
}