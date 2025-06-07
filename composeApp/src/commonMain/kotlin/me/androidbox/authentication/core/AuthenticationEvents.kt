package me.androidbox.authentication.core

sealed interface AuthenticationEvents {

    data object OnAuthenticationSuccess: AuthenticationEvents
    data class OnAuthenticationFail(val message: String) : AuthenticationEvents
}