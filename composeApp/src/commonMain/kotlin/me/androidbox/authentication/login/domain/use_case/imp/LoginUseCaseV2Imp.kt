package me.androidbox.authentication.login.domain.use_case.imp

import me.androidbox.authentication.login.domain.model.LoginRequest
import me.androidbox.authentication.login.domain.model.LoginResponse
import me.androidbox.authentication.login.domain.use_case.LoginUseCaseV2
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.models.DataError
import net.orandja.either.Either

class LoginUseCaseV2Imp(
    private val authorizationRepository: AuthorizationRepository,
) : LoginUseCaseV2 {
    override suspend fun execute(loginRequest: LoginRequest): Either<LoginResponse, DataError.Network> {
        return authorizationRepository.loginV2(
            loginRequest = loginRequest
        )
    }
}