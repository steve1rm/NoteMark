package me.androidbox.authentication.register.data.imp

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import me.androidbox.authentication.login.data.LoginRequestDto
import me.androidbox.authentication.login.data.LoginResponseDto
import me.androidbox.authentication.login.data.LogoutRequestDto
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

       // For some reason it can't find providers httpClient.plugin(Auth).providers

        return safeResult
    }

    override suspend fun logoutUser(logoutRequestDto: LogoutRequestDto): Either<Unit, DataError.Network> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.LOGOUT) {
                    this.setBody(logoutRequestDto)
                }

            response
        }

        // QUESTION: providers is not recognized
//         httpClient.plugin(Auth).providers

        // FEEDBACK: Has been changed to this in Ktor 3.x
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()

        return safeResult
    }
}