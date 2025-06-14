package me.androidbox.authentication.login.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val accessToken: String,
    val refreshToken: String
)
