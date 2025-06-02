package me.androidbox.authentication.login.presentation

sealed interface LoginActions {
    data class OnEmailChange(val value: String) : LoginActions
    data class OnPasswordChange(val value: String) : LoginActions
    data object OnToggleShowPassword : LoginActions
}