package me.androidbox.authentication.login.domain.use_case

import me.androidbox.core.models.DataError
import net.orandja.either.Either

fun interface LoginUseCaseV2 {
    suspend fun execute(username: String): Either<Unit, DataError.Network>
}