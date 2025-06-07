package me.androidbox.authentication.login.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import me.androidbox.core.models.DataError
import net.orandja.either.Left
import net.orandja.either.Right

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

            LoginActions.OnLogin -> {
                viewModelScope.launch {
                    try {
                        _state.update { it.copy(isLoading = true) }
                        val result = loginUseCase.execute(
                            email = state.value.email,
                            password = state.value.password
                        )

                        when(result) {
                            is Left -> {
                                _state.update { loginUiState ->
                                    loginUiState.copy(isLoading = false)
                                }
                                _events.send(LoginEvents.OnLoginSuccess)
                            }
                            is Right<DataError> -> {
                                _events.send(LoginEvents.OnLoginFail(result.value))
                            }
                        }
                    }
                    catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }

            is LoginActions.OnSendMessage -> {
                sendMessage(loginActions.message)
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