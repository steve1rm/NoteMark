package me.androidbox.authentication.login.data

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefresh(
    val refreshToken: String
)
