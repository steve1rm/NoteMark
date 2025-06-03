package me.androidbox.authentication.register.domain.use_case

import me.androidbox.authentication.register.domain.model.ValidationResult

class ValidateUsernameUseCase {

    operator fun invoke(username: String): ValidationResult {
        if (username.length < 3) {
            return ValidationResult.Error("Username must be at least 3 characters long.")
        }
        if (username.length > 20) {
            return ValidationResult.Error("Username must be less than 20 characters.")
        }
        return ValidationResult.Valid
    }
}
