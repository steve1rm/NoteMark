package me.androidbox.authentication.register.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.androidbox.authentication.register.domain.use_case.ValidateEmailUseCase
import me.androidbox.authentication.register.domain.use_case.ValidatePasswordUseCase
import me.androidbox.authentication.register.domain.use_case.ValidateRepeatPasswordUseCase
import me.androidbox.authentication.register.domain.use_case.ValidateUsernameUseCase

class RegisterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val usernameValidationUseCase = ValidateUsernameUseCase()
            val emailValidationUseCase = ValidateEmailUseCase()
            val passwordValidationUseCase = ValidatePasswordUseCase()
            val repeatPasswordValidationUseCase = ValidateRepeatPasswordUseCase()

            return RegisterViewModel(
                validateUsernameUseCase = usernameValidationUseCase,
                validateEmailUseCase = emailValidationUseCase,
                validatePasswordUseCase = passwordValidationUseCase,
                validateRepeatPasswordUseCase = repeatPasswordValidationUseCase
            ) as T
        }
        throw IllegalStateException("Viewmodel not found")
    }
}