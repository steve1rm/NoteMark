package me.androidbox.authentication.register.presentation

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val repeatPasswordError: String? = null,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isRegisterEnabled: Boolean = false
)
