package me.androidbox.authentication.login.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
