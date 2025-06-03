package me.androidbox.authentication.register.domain.use_case

import me.androidbox.authentication.register.domain.model.ValidationResult

class ValidateRepeatPasswordUseCase {
    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        return if (password == repeatedPassword)
            ValidationResult.Valid
        else ValidationResult.Error("Passwords do not match")
    }
}