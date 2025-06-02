package me.androidbox.authentication.login.presentation.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.androidbox.authentication.login.domain.use_case.ValidateLoginUseCase
import me.androidbox.authentication.login.presentation.LoginActions
import me.androidbox.authentication.login.presentation.LoginUiState

class LoginViewModel (
    private val validateLoginUseCase: ValidateLoginUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onAction(loginActions: LoginActions) {
        when (loginActions) {
            is LoginActions.OnEmailChange -> {
                _state.update { it.copy(email = loginActions.value) }
                validateLogin(_state.value.email, _state.value.password)
            }

            is LoginActions.OnPasswordChange -> {
                _state.update { it.copy(password = loginActions.value) }
                validateLogin(_state.value.email, _state.value.password)
            }

            LoginActions.OnToggleShowPassword -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
        }
    }

    private fun validateLogin(email: String, password: String) {
        val isLoginValid = validateLoginUseCase(email, password)
        _state.update { it.copy(isLoginEnabled = isLoginValid) }
    }
}