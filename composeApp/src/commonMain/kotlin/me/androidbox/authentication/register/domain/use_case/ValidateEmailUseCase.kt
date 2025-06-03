package me.androidbox.authentication.register.domain.use_case

import android.util.Patterns
import me.androidbox.authentication.register.domain.model.ValidationResult

class ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationResult {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return if (isEmailValid)
            ValidationResult.Valid
        else ValidationResult.Error("Invalid email provided")
    }
}