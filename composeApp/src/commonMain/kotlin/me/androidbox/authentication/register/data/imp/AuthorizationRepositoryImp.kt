package me.androidbox.authentication.register.data.imp

import me.androidbox.authentication.login.data.LoginDto
import me.androidbox.authentication.login.data.LoginV2Dto
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

    override suspend fun login(loginDto: LoginDto): Either<Unit, DataError.Network> {
        return authorizationRemoteDataSource.loginUser(loginDto)
    }

    override suspend fun loginV2(loginV2Dto: LoginV2Dto): Either<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }
}
