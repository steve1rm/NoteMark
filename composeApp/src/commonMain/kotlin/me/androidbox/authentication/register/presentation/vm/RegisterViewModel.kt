package me.androidbox.authentication.register.presentation.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.androidbox.authentication.register.domain.use_case.ValidateEmailUseCase
import me.androidbox.authentication.register.domain.model.ValidationResult
import me.androidbox.authentication.register.domain.use_case.ValidatePasswordUseCase
import me.androidbox.authentication.register.domain.use_case.ValidateRepeatPasswordUseCase
import me.androidbox.authentication.register.domain.use_case.ValidateUsernameUseCase
import me.androidbox.authentication.register.presentation.RegisterActions
import me.androidbox.authentication.register.presentation.RegisterUiState

class RegisterViewModel(
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateRepeatPasswordUseCase: ValidateRepeatPasswordUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterActions) {
        when (action) {
            is RegisterActions.OnUsernameChange -> {
                _state.update { it.copy(username = action.username) }
                validateUsername(username = action.username)
            }

            is RegisterActions.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
                validateEmail(email = action.email)
            }

            is RegisterActions.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
                validatePassword(password = action.password)
            }

            is RegisterActions.OnRepeatPasswordChange -> {
                _state.update { it.copy(repeatPassword = action.confirmPassword) }
                validateRepeatPassword(
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
        }
    }

    private fun validateUsername(username: String) {
        val validatedUsernameResult = validateUsernameUseCase(username)

        when (val state = validatedUsernameResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(usernameError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(usernameError = null) }
            }
        }
    }

    private fun validateEmail(email: String) {
        val validatedEmailResult = validateEmailUseCase(email)

        when (val state = validatedEmailResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(emailError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(emailError = null) }
            }
        }
    }

    private fun validatePassword(password: String) {
        val validatedPasswordResult = validatePasswordUseCase(password)

        when (val state = validatedPasswordResult) {
            is ValidationResult.Error -> {
                _state.update { it.copy(passwordError = state.message) }
            }

            ValidationResult.Valid -> {
                _state.update { it.copy(passwordError = null) }
            }
        }
    }

    private fun validateRepeatPassword(password: String, repeatPassword: String) {
        val validatedRepeatPasswordResult = validateRepeatPasswordUseCase(
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
}