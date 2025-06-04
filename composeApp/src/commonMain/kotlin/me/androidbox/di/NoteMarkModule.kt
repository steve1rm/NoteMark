package me.androidbox.di

import me.androidbox.authentication.login.presentation.vm.LoginViewModel
import me.androidbox.authentication.register.presentation.vm.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val noteMarkModule = module {
    viewModelOf(::RegisterViewModel)

    viewModelOf(::LoginViewModel)


}