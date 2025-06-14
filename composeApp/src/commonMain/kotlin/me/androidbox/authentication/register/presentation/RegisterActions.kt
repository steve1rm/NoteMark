package me.androidbox.authentication.register.presentation

sealed interface RegisterActions {
    data class OnUsernameChange(val username: String) : RegisterActions
    data class OnEmailChange(val email: String) : RegisterActions
    data class OnPasswordChange(val password: String) : RegisterActions
    data class OnRepeatPasswordChange(val confirmPassword: String) : RegisterActions
    data object OnToggleShowPassword : RegisterActions
    data object OnToggleShowConfirmPassword : RegisterActions
    data object OnRegister : RegisterActions

    data class OnSendMessage(val message: String) : RegisterActions
}