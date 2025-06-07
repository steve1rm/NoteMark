package me.androidbox.authentication.login.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvents>()
    val events = _events.receiveAsFlow()

    fun onAction(loginActions: LoginActions) {
        when (loginActions) {
            is LoginActions.OnEmailChange -> {
                _state.update { it.copy(email = loginActions.email) }
                validateLogin(_state.value.email, _state.value.password)
            }

            is LoginActions.OnPasswordChange -> {
                _state.update { it.copy(password = loginActions.password) }
                validateLogin(_state.value.email, _state.value.password)
            }

            LoginActions.OnToggleShowPassword -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }

            is LoginActions.OnLoginClick -> {
                startLogin(loginActions.email, loginActions.password)
            }

            is LoginActions.OnSendMessage -> {
                sendMessage(loginActions.message)
            }
        }
    }

    private fun startLogin(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(email, password).onSuccess {
                _events.send(LoginEvents.OnLoginSuccess)
            }.onFailure {
                _events.send(LoginEvents.OnLoginFail)
            }

        }
    }

    private fun validateLogin(email: String, password: String) {
        val isLoginValid = validateEmailAndPassword(email, password)
        _state.update { it.copy(isLoginEnabled = isLoginValid) }
    }

    private fun validateEmailAndPassword(email: String, password: String): Boolean {
        val isEmailValid = email.isNotEmpty() && isEmailValid(email)
        val isPasswordValid = password.isNotEmpty()
        return isEmailValid && isPasswordValid
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            _state.update { it.copy(message = message) }
            delay(3000)
            _state.update { it.copy(message = null) }
        }
    }
}