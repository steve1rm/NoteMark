package me.androidbox.authentication.register.domain.use_case

import me.androidbox.authentication.register.domain.model.ValidationResult

class ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationResult {
        val hasValidLength = password.length >= 8
        val hasDigit = password.any { it.isDigit() }

        return if (hasValidLength && hasDigit)
            ValidationResult.Valid
        else ValidationResult.Error("Password must be at least 8 characters and include a number or symbol.")
    }
}