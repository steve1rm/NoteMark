package me.androidbox.authentication.register.data.imp

import io.ktor.client.*
import io.ktor.client.request.*
import me.androidbox.authentication.login.data.LoginDto
import me.androidbox.authentication.login.data.LoginV2Dto
import me.androidbox.authentication.register.data.AuthorizationRemoteDataSource
import me.androidbox.authentication.register.data.Register
import me.androidbox.core.data.Routes
import me.androidbox.core.data.safeApiRequest
import me.androidbox.core.models.DataError
import net.orandja.either.Either

class AuthorizationRemoteDataSourceImp(
    private val httpClient: HttpClient
) : AuthorizationRemoteDataSource {
    override suspend fun registerUser(register: Register): Either<Unit, DataError> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.REGISTRATION) {
                    this.setBody(register)
                }
            response
        }

        return safeResult
    }

    override suspend fun loginUser(loginDto: LoginDto): Either<Unit, DataError.Network> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.LOGIN) {
                    this.setBody(loginDto)
                }
            response
        }

        return safeResult
    }

    override suspend fun loginUserV2(loginV2Dto: LoginV2Dto): Either<Unit, DataError.Network> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.LOGIN) {
                    this.setBody(loginV2Dto)
                }

            response
        }

        return safeResult
    }
}