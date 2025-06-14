package me.androidbox.authentication.login.presentation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)