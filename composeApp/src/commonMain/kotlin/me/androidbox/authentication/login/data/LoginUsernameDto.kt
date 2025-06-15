package me.androidbox.authentication.login.data

data class LoginUsernameDto(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
