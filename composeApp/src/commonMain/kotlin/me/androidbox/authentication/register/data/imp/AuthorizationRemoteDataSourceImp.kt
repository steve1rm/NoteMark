package me.androidbox.authentication.register.data.imp

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import me.androidbox.authentication.login.data.Login
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
                .post(Routes.REGISTATION) {
                    this.setBody(register)
                }
            response
        }

        return safeResult
    }

    override suspend fun loginUser(login: Login): Either<Unit, DataError.Network> {
        val safeResult = safeApiRequest<Unit> {
            val response = httpClient
                .post(Routes.LOGIN) {
                    this.setBody(login)
                }
            response
        }

        return safeResult
    }
}