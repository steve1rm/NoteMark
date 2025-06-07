package me.androidbox.authentication.login.presentation

sealed interface LoginActions {
    data class OnEmailChange(val email: String) : LoginActions
    data class OnPasswordChange(val password: String) : LoginActions
    data object OnToggleShowPassword : LoginActions
    data class OnLoginClick(
        val email: String,
        val password: String
    ) : LoginActions
    data class OnSendMessage(val message: String) : LoginActions
}