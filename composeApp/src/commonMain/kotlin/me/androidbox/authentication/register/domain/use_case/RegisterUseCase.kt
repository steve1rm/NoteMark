package me.androidbox.authentication.register.domain.use_case

import me.androidbox.authentication.register.data.Register
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.models.DataError
import net.orandja.either.Either

class RegisterUseCase(
    private val authorizationRepository: AuthorizationRepository
) {

    suspend fun execute(
        username: String,
        email: String,
        password: String
    ): Either<Unit, DataError> {
        return authorizationRepository.register(
            Register(
                username = username,
                email = email,
                password = password
            )
        )
    }
}