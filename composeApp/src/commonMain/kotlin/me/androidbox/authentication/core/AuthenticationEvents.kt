package me.androidbox.authentication.core

sealed interface AuthenticationEvents {

    data class OnAuthenticationSuccess(val username: String): AuthenticationEvents
    data class OnAuthenticationFail(val message: String) : AuthenticationEvents
}