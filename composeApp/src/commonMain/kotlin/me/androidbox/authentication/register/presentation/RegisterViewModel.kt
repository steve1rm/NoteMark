package me.androidbox.authentication.register.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.core.AuthenticationEvents
import me.androidbox.authentication.register.domain.use_case.RegisterUseCase
import me.androidbox.authentication.register.presentation.model.ValidationResult
import me.androidbox.core.models.DataError
import me.androidbox.emailValid
import me.androidbox.settings.presentation.model.SyncInterval
import me.androidbox.user.domain.User
import me.androidbox.user.domain.UserRepository
import net.orandja.either.Left
import net.orandja.either.Right

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val userRepository: UserRepository,
    private val noteMarkPreferences: NoteMarkPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthenticationEvents>()
    val events = _events.receiveAsFlow()

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
                startRegisterProcess()
            }

            is RegisterActions.OnSendMessage -> {
                sendMessage(action.message)
            }
        }
        checkRegisterButtonEnabled()
    }

    private fun startRegisterProcess() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val result = registerUseCase.execute(
                    username = state.value.username,
                    email = state.value.email,
                    password = state.value.password
                )

                when (result) {
                    is Left -> {
                        _state.update { registerUiState ->
                            registerUiState.copy(isLoading = false)
                        }
                        userRepository.saveUser(
                            User(
                                userName = state.value.username,
                                syncInterval = SyncInterval.MANUAL,
                                syncTimeStamp = 0L
                            )
                        )

                        noteMarkPreferences.setUserName(state.value.username)

                        _events.send(AuthenticationEvents.OnAuthenticationSuccess(state.value.username))
                    }

                    is Right<DataError> -> {
                        _state.update { registerUiState ->
                            registerUiState.copy(isLoading = false)
                        }
                        val message = if (result.right is DataError.Network) {
                            "Error occurred"
                        } else {
                            "Invalid"
                        }

                        _events.send(AuthenticationEvents.OnAuthenticationFail(message))
                    }
                }
            } catch (exception: Exception) {
                _state.update { registerUiState ->
                    registerUiState.copy(isLoading = false)
                }
                _events.send(AuthenticationEvents.OnAuthenticationFail("Error occurred"))
                exception.printStackTrace()
            }
        }
    }

    private fun checkRegisterButtonEnabled() {
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

    private fun validateEmail(email: String): ValidationResult {
        val isEmailValid = emailValid(email)
        return if (isEmailValid)
            ValidationResult.Valid
        else ValidationResult.Error("Invalid email provided")
    }

    private fun validateRepeatPassword(
        password: String,
        repeatedPassword: String
    ): ValidationResult {
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

    private fun validateUsername(username: String): ValidationResult {
        if (username.length < 3) {
            return ValidationResult.Error("Username must be at least 3 characters long.")
        }
        if (username.length > 20) {
            return ValidationResult.Error("Username must be less than 20 characters.")
        }
        return ValidationResult.Valid
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            _state.update { it.copy(message = message) }
            delay(3000)
            _state.update { it.copy(message = null) }
        }
    }
}