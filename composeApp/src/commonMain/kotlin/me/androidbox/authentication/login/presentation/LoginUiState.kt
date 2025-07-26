package me.androidbox.authentication.login.presentation

data class LoginUiState(
    val email: String = "steve@mail.com",
    val password: String = "qwerty@1",
    val showPassword: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)