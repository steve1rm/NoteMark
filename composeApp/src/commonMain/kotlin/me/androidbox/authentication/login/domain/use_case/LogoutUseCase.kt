package me.androidbox.authentication.login.domain.use_case

import me.androidbox.core.data.models.DataError
import net.orandja.either.Either

fun interface LogoutUseCase {
    suspend fun execute(): Either<Unit, DataError>
}