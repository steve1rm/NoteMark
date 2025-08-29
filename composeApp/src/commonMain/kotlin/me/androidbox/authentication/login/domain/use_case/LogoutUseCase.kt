package me.androidbox.authentication.login.domain.use_case

import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

fun interface LogoutUseCase {
    suspend fun execute(logoutRequest: LogoutRequest): Either<Unit, DataError.Network>
}