package me.androidbox.authentication.login.domain.use_case

import me.androidbox.authentication.login.domain.model.LoginRequest
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

class LoginUseCase(private val authorizationRepository: AuthorizationRepository) {

    suspend fun execute(email: String, password: String): Either<Unit, DataError.Network> {
        val response = authorizationRepository.login(
            LoginRequest(
                email = email,
                password = password
            )
        )

        return response
    }
}