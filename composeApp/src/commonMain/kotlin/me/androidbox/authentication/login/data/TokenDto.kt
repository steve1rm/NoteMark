package me.androidbox.authentication.login.data

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val accessToken: String,
    val refreshToken: String
)