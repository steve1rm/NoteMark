package me.androidbox.authentication.login.domain.use_case

import me.androidbox.authentication.login.data.datasource.LoginDataSource

class LoginUseCase(
    val loginDataSource: LoginDataSource
) {
    suspend operator fun invoke(email: String, password: String) : Result<Unit> {
        val response = loginDataSource.login(email, password)
        return if(response.isSuccess) {
            Result.success(Unit)
        } else Result.failure(Exception(""))
    }

}