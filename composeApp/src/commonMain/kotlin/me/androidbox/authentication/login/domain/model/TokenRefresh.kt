package me.androidbox.authentication.login.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefresh(
    val refreshToken: String
)
