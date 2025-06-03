package me.androidbox.authentication.register.presentation.model

data class RegisterError(
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val repeatPasswordError: String? = null,
    val registerButtonEnabled: Boolean = false
)
