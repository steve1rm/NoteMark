package me.androidbox.authentication.register.data.imp

import me.androidbox.authentication.login.data.toLoginRequestDto
import me.androidbox.authentication.login.data.toLoginResponse
import me.androidbox.authentication.login.data.toLogoutRequestDto
import me.androidbox.authentication.login.domain.model.LoginRequest
import me.androidbox.authentication.login.domain.model.LoginResponse
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.register.data.AuthorizationRemoteDataSource
import me.androidbox.authentication.register.data.RegisterDto
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.models.DataError
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class AuthorizationRepositoryImp(
    private val authorizationRemoteDataSource: AuthorizationRemoteDataSource
) : AuthorizationRepository {
    override suspend fun register(registerDto: RegisterDto): Either<Unit, DataError> {
        return authorizationRemoteDataSource.registerUser(registerDto)
    }

    override suspend fun login(loginRequest: LoginRequest): Either<Unit, DataError.Network> {
        return authorizationRemoteDataSource.loginUser(loginRequest.toLoginRequestDto())
    }

    override suspend fun loginV2(loginRequest: LoginRequest): Either<LoginResponse, DataError.Network> {
        val result = authorizationRemoteDataSource.loginUserV2(loginRequest.toLoginRequestDto())

        return  when(result) {
            is Left -> {
                Left(result.left.toLoginResponse())
            }
            is Right -> Right(result.right)
        }
    }

    override suspend fun logout(logoutRequest: LogoutRequest): Either<Unit, DataError.Network> {
        val result = authorizationRemoteDataSource.logoutUser(logoutRequest.toLogoutRequestDto())

        return when(result) {
            is Left -> {
                Left(Unit)
            }
            is Right -> {
                Right(result.right)
            }
        }
    }
}
