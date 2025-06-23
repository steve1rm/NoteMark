package me.androidbox.authentication.login.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginV2Dto(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
