package me.androidbox.authentication.register.data.imp

import io.ktor.client.*
import io.ktor.client.request.*
import me.androidbox.authentication.login.data.LoginRequestDto
import me.androidbox.authentication.login.data.LoginResponseDto
import me.androidbox.authentication.register.data.AuthorizationRemoteDataSource
import me.androidbox.authentication.register.data.RegisterDto
import me.androidbox.core.data.Routes
import me.androidbox.core.data.safeApiRequest
import me.androidbox.core.models.DataError
import net.orandja.either.Either

class AuthorizationRemoteDataSourceImp(
    private val httpClient: HttpClient
) : AuthorizationRemoteDataSource {
    override suspend fun registerUser(registerDto: RegisterDto): Either<Unit, DataError> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.REGISTRATION) {
                    this.setBody(registerDto)
                }
            response
        }

        return safeResult
    }

    override suspend fun loginUser(loginRequestDto: LoginRequestDto): Either<Unit, DataError.Network> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.LOGIN) {
                    this.setBody(loginRequestDto)
                }
            response
        }

        return safeResult
    }

    override suspend fun loginUserV2(loginRequestDto: LoginRequestDto): Either<LoginResponseDto, DataError.Network> {
        val safeResult = safeApiRequest<LoginResponseDto> {
            val response = httpClient
                .post(Routes.LOGIN) {
                    this.setBody(loginRequestDto)
                }

            response
        }

        return safeResult
    }
}