package me.androidbox.authentication.register.data

import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val username: String,
    val email: String,
    val password: String
)