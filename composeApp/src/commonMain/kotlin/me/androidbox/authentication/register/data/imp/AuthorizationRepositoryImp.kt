package me.androidbox.authentication.register.data.imp

import me.androidbox.authentication.login.data.Login
import me.androidbox.authentication.register.data.AuthorizationRemoteDataSource
import me.androidbox.authentication.register.data.Register
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.models.DataError
import net.orandja.either.Either

class AuthorizationRepositoryImp(
    private val authorizationRemoteDataSource: AuthorizationRemoteDataSource
) : AuthorizationRepository {
    override suspend fun register(register: Register): Either<Unit, DataError> {
        return authorizationRemoteDataSource.registerUser(register)
    }

    override suspend fun login(login: Login): Either<Unit, DataError> {
        return authorizationRemoteDataSource.loginUser(login)
    }
}
