package me.androidbox.authentication.login.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.androidbox.authentication.login.domain.use_case.ValidateLoginUseCase

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val validatorUseCase = ValidateLoginUseCase()
            return LoginViewModel(validatorUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}