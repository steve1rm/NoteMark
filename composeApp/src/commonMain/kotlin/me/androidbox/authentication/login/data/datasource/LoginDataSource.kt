package me.androidbox.authentication.login.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import me.androidbox.authentication.login.domain.model.Login
import me.androidbox.authentication.login.domain.model.TokenDto
import me.androidbox.core.data.Routes.LOGIN
import me.androidbox.core.models.Constants.EMAIL

class LoginDataSource(
    private val httpNetworkClient: HttpClient
) {

    suspend fun login(email: String, password: String): Result<TokenDto> {
        val response = httpNetworkClient.post(LOGIN) {
            headers {
                append("X-User-Email", EMAIL)
            }
            setBody(Login(email, password))
        }

        return if (response.status.isSuccess()) {
            Result.success(response.body<TokenDto>())
        } else {
            Result.failure(Exception("Error in login"))
        }
    }
}