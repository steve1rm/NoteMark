package me.androidbox.authentication.login.domain.use_case

import me.androidbox.authentication.login.domain.model.LoginRequest
import me.androidbox.authentication.login.domain.model.LoginResponse
import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

fun interface LoginUseCaseV2 {
    suspend fun execute(loginRequest: LoginRequest): Either<LoginResponse, DataError.Network>
}