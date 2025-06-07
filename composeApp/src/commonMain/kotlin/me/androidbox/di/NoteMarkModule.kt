package me.androidbox.di

import io.ktor.client.HttpClient
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import me.androidbox.authentication.login.presentation.LoginViewModel
import me.androidbox.authentication.register.data.AuthorizationRemoteDataSource
import me.androidbox.authentication.register.data.imp.AuthorizationRemoteDataSourceImp
import me.androidbox.authentication.register.data.imp.AuthorizationRepositoryImp
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.authentication.register.domain.use_case.RegisterUseCase
import me.androidbox.authentication.register.presentation.RegisterViewModel
import me.androidbox.core.data.imp.HttpNetworkClientImp
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val noteMarkModule = module {

    factory { LoginUseCase(
        get<AuthorizationRepository>()
    ) }

    factory { RegisterUseCase(
        get<AuthorizationRepository>())
    }

    factory<AuthorizationRepository> {
        AuthorizationRepositoryImp(
            get<AuthorizationRemoteDataSource>()
        )
    }

    factory<AuthorizationRemoteDataSource> {
        AuthorizationRemoteDataSourceImp(
            get<HttpClient>()
        )
    }

    viewModelOf(::RegisterViewModel)

    viewModelOf(::LoginViewModel)

    single<HttpClient> {
        HttpNetworkClientImp(get<NoteMarkPreferences>())
            .build()
    }
}
