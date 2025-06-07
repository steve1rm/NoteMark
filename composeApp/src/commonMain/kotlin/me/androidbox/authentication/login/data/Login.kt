package me.androidbox.authentication.login.data

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val email: String,
    val password: String
)
