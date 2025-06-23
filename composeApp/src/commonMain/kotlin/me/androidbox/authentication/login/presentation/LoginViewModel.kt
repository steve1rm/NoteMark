package me.androidbox.authentication.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.androidbox.authentication.core.AuthenticationEvents
import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import me.androidbox.authentication.login.domain.use_case.LoginUseCaseV2
import me.androidbox.core.models.DataError
import me.androidbox.emailValid
import net.orandja.either.Left
import net.orandja.either.Right

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginUseCaseV2: LoginUseCaseV2
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthenticationEvents>()
    val events = _events.receiveAsFlow()

    init {
        /** observe changes to the email and password */
        state
            .distinctUntilChanged { old, new ->
                old.email == new.email && old.password == new.password
            }
            .onEach { loginUiStateSuccess ->
                validateLogin(loginUiStateSuccess.email, loginUiStateSuccess.password)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(loginActions: LoginActions) {
        when (loginActions) {
            is LoginActions.OnEmailChange -> {
                _state.update { it.copy(email = loginActions.value) }
            }

            is LoginActions.OnPasswordChange -> {
                _state.update { it.copy(password = loginActions.value) }
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
                                _events.send(AuthenticationEvents.OnAuthenticationSuccess)
                            }
                            is Right<DataError> -> {
                                _state.update { loginUiState ->
                                    loginUiState.copy(isLoading = false)
                                }
                                val message = if(result.right is DataError.Network) {
                                    "Invalid login credentials"
                                } else {
                                    "Invalid"
                                }
                                _events.send(AuthenticationEvents.OnAuthenticationFail(message))
                            }
                        }
                    }
                    catch (exception: Exception) {
                        _state.update { loginUiState ->
                            loginUiState.copy(isLoading = false)
                        }
                        _events.send(AuthenticationEvents.OnAuthenticationFail("Invalid login credentials"))
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
        return emailValid(email)
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            _state.update { it.copy(message = message) }
            delay(3000)
            _state.update { it.copy(message = null) }
        }
    }
}