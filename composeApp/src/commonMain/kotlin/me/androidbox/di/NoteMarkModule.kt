package me.androidbox.di

import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import me.androidbox.authentication.login.presentation.LoginViewModel
import me.androidbox.authentication.register.domain.use_case.RegisterUseCase
import me.androidbox.authentication.register.presentation.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val noteMarkModule = module {

    single { LoginUseCase() }

    single { RegisterUseCase() }

    viewModelOf(::RegisterViewModel)

    viewModelOf(::LoginViewModel)


}