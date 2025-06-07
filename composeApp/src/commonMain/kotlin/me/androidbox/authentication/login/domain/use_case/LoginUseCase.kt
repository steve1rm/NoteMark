package me.androidbox.authentication.login.domain.use_case

import me.androidbox.authentication.login.data.Login
import me.androidbox.authentication.register.domain.AuthorizationRepository

class LoginUseCase(private val authorizationRepository: AuthorizationRepository) {

    suspend fun execute(email: String, password: String): Result<Unit> {
        val response = authorizationRepository.login(
            Login(
                email = email,
                password = password
            )
        )
        val error = response.rightOrNull
        return if (error == null) Result.success(Unit)
        else Result.failure(Exception("Login failed"))
    }
}