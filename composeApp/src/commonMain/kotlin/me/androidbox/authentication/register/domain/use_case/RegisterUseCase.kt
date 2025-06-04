package me.androidbox.authentication.register.domain.use_case

class RegisterUseCase (
    val validateUsernameUseCase: ValidateUsernameUseCase,
    val validateEmailUseCase: ValidateEmailUseCase,
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val validateRepeatPasswordUseCase: ValidateRepeatPasswordUseCase,
) {

    fun register(
        username: String,
        email: String,
        password: String
    ) {
        //TODO()
    }
}