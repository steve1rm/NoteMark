package me.androidbox.di

import io.ktor.client.HttpClient
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.data.datasource.LoginDataSource
import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import me.androidbox.authentication.login.presentation.LoginViewModel
import me.androidbox.authentication.register.domain.use_case.RegisterUseCase
import me.androidbox.authentication.register.presentation.RegisterViewModel
import me.androidbox.core.data.imp.HttpNetworkClientImp
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val noteMarkModule = module {

    single<HttpClient> {
        HttpNetworkClientImp(get<NoteMarkPreferences>())
            .build()
    }

    factory<LoginUseCase> { LoginUseCase(LoginDataSource(get())) }

    factory { RegisterUseCase() }

    viewModelOf(::RegisterViewModel)

    viewModelOf(::LoginViewModel)

}
