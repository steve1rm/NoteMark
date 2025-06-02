package me.androidbox.authentication.register.presentation.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.androidbox.authentication.register.domain.ValidateRegisterUseCase
import me.androidbox.authentication.register.presentation.RegisterActions
import me.androidbox.authentication.register.presentation.RegisterUiState

class RegisterViewModel (
    private val validateRegisterUseCase: ValidateRegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterActions) {
        when (action) {
            is RegisterActions.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = action.confirmPassword) }
            }
            is RegisterActions.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }
            is RegisterActions.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }
            RegisterActions.OnToggleShowConfirmPassword -> {
                _state.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            }
            RegisterActions.OnToggleShowPassword -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
            is RegisterActions.OnUsernameChange -> {
                _state.update { it.copy(username = action.username) }
            }
        }
    }

//    private fun validateRegister(email: String, password: String) {
//        val isLoginValid = validateRegisterUseCase(email, password)
//        _state.update { it.copy(isLoginEnabled = isLoginValid) }
//    }
}