package me.androidbox.authentication.register.presentation

import android.util.Patterns
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.authentication.register.domain.model.ValidationResult
import me.androidbox.authentication.register.domain.use_case.RegisterUseCase

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterActions) {
        when (action) {
            is RegisterActions.OnUsernameChange -> {
                _state.update { it.copy(username = action.username) }
                checkUsername(username = action.username)
            }

            is RegisterActions.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
                checkEmail(email = action.email)
            }

            is RegisterActions.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
                checkPassword(password = action.password)
            }

            is RegisterActions.OnRepeatPasswordChange -> {
                _state.update { it.copy(repeatPassword = action.confirmPassword) }
                checkRepeatPassword(
                    password = _state.value.password,
                    repeatPassword = action.confirmPassword
                )
            }

            RegisterActions.OnToggleShowPassword -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }

            RegisterActions.OnToggleShowConfirmPassword -> {
                _state.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            }

            RegisterActions.OnRegister -> {
                viewModelScope.launch {
                    try {
                        registerUseCase.register(
                            username = state.value.username,
                            password = state.value.password,
                            email = state.value.email
                        )
                    }
                    catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }
        }

        viewModelScope.launch {
            snapshotFlow { _state.value }
                .collect {
                    if (
                        it.usernameError == null &&
                        it.emailError == null &&
                        it.passwordError == null &&
                        it.repeatPasswordError == null &&
                        it.username.isNotEmpty() &&
                        it.email.isNotEmpty() &&
                        it.password.isNotEmpty() &&
                        it.repeatPassword.isNotEmpty()
                    ) {
                        _state.update { state -> state.copy(isRegisterEnabled = true) }
                    } else {
                        _state.update { state -> state.copy(isRegisterEnabled = false) }
                    }
                }
        }
    }

    private fun checkUsername(username: String) {
        val validatedUsernameResult = validateUsername(username)

        when (val state = validatedUsernameResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(usernameError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(usernameError = null) }
            }
        }
    }

    private fun checkEmail(email: String) {
        val validatedEmailResult = validateEmail(email)

        when (val state = validatedEmailResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(emailError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(emailError = null) }
            }
        }
    }

    private fun checkPassword(password: String) {
        val validatedPasswordResult = validatePassword(password)

        when (val state = validatedPasswordResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(passwordError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(passwordError = null) }
            }
        }
    }

    private fun checkRepeatPassword(password: String, repeatPassword: String) {
        val validatedRepeatPasswordResult = validateRepeatPassword(
            password = password,
            repeatedPassword = repeatPassword
        )

        when (val state = validatedRepeatPasswordResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(repeatPasswordError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(repeatPasswordError = null) }
            }
        }
    }

    private fun validateEmail(email: String) : ValidationResult {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return if (isEmailValid)
            ValidationResult.Valid
        else ValidationResult.Error("Invalid email provided")
    }

    private fun validateRepeatPassword(password: String, repeatedPassword: String): ValidationResult {
        return if (password == repeatedPassword)
            ValidationResult.Valid
        else ValidationResult.Error("Passwords do not match")
    }

    private fun validatePassword(password: String): ValidationResult {
        val hasValidLength = password.length >= 8
        val hasDigit = password.any { it.isDigit() }

        return if (hasValidLength && hasDigit)
            ValidationResult.Valid
        else ValidationResult.Error("Password must be at least 8 characters and include a number or symbol.")
    }

    private fun validateUsername(username: String) : ValidationResult {
        if (username.length < 3) {
            return ValidationResult.Error("Username must be at least 3 characters long.")
        }
        if (username.length > 20) {
            return ValidationResult.Error("Username must be less than 20 characters.")
        }
        return ValidationResult.Valid
    }
}