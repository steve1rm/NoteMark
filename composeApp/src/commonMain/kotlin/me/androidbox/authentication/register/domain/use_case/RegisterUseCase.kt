package me.androidbox.authentication.register.domain.use_case

import me.androidbox.authentication.register.data.Register
import me.androidbox.authentication.register.domain.AuthorizationRepository

class RegisterUseCase(
    private val authorizationRepository: AuthorizationRepository
) {

    suspend fun execute(
        username: String,
        email: String,
        password: String
    ) {
        authorizationRepository.register(
            Register(
                username = username,
                email = email,
                password = password
            )
        )
    }
}