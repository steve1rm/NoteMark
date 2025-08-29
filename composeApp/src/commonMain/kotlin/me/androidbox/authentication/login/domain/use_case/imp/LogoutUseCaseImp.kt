package me.androidbox.authentication.login.domain.use_case.imp

import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

class LogoutUseCaseImp(private val authorizationRepository: AuthorizationRepository) : LogoutUseCase {
    override suspend fun execute(logoutRequest: LogoutRequest): Either<Unit, DataError.Network> {
        return authorizationRepository.logout(logoutRequest)
    }
}