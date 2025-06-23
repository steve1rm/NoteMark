package me.androidbox.authentication.login.domain.use_case.imp

import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.data.LoginV2Dto
import me.androidbox.authentication.login.domain.use_case.LoginUseCaseV2
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.models.DataError
import net.orandja.either.Either

class LoginUseCaseV2Imp(
    private val authorizationRepository: AuthorizationRepository,
    private val noteMarkPreferences: NoteMarkPreferences
) : LoginUseCaseV2 {
    override suspend fun execute(username: String): Either<Unit, DataError.Network> {
        val refreshToken = noteMarkPreferences.getRefreshToken() ?: ""
        val accessToken = noteMarkPreferences.getAccessToken() ?: ""
        val loginV2Dto = LoginV2Dto(
            refreshToken = refreshToken,
            accessToken = accessToken,
            username = username
        )
        return authorizationRepository.loginV2(loginV2Dto)
    }
}