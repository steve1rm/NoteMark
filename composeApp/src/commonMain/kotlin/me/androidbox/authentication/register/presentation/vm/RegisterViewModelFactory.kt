package me.androidbox.authentication.register.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.androidbox.authentication.register.domain.ValidateRegisterUseCase

class RegisterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val useCase = ValidateRegisterUseCase()
            return RegisterViewModel(useCase) as T
        }
        throw IllegalStateException("Viewmodel not found")
    }
}