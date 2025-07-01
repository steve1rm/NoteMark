package me.androidbox.authentication.login.domain.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
