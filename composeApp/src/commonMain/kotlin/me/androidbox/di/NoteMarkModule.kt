package me.androidbox.di

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.use_case.LoginUseCase
import me.androidbox.authentication.login.presentation.LoginViewModel
import me.androidbox.authentication.register.data.AuthorizationRemoteDataSource
import me.androidbox.authentication.register.data.imp.AuthorizationRemoteDataSourceImp
import me.androidbox.authentication.register.data.imp.AuthorizationRepositoryImp
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.authentication.register.domain.use_case.RegisterUseCase
import me.androidbox.authentication.register.presentation.RegisterViewModel
import me.androidbox.core.data.NoteMarkDatabase
import me.androidbox.core.data.imp.HttpNetworkClientImp
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.data.datasources.imp.NotesLocalDataSourceImp
import me.androidbox.notes.data.datasources.imp.NotesRemoteDataSourceImp
import me.androidbox.notes.data.repository.NotesRepositoryImp
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import me.androidbox.notes.domain.usecases.FetchNotesUseCase
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import me.androidbox.notes.domain.usecases.imp.DeleteNoteUseCaseImp
import me.androidbox.notes.domain.usecases.imp.FetchNotesUseCaseImp
import me.androidbox.notes.domain.usecases.imp.SaveNoteUseCaseImp
import me.androidbox.notes.presentation.EditNoteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val noteMarkModule = module {
    /** ViewModels */
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModel {
        EditNoteViewModel(get<SaveNoteUseCase>())
    }

    factory<SaveNoteUseCase> { SaveNoteUseCaseImp(get()) }


    /** UseCases */
//    factory { SaveNoteUseCaseImp(get<NotesRepository>()) }.bind(SaveNoteUseCase::class)
    factory { DeleteNoteUseCaseImp() }.bind(DeleteNoteUseCase::class)
    factory { FetchNotesUseCaseImp() }.bind(FetchNotesUseCase::class)

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

    single<CoroutineDispatcher> { Dispatchers.IO }

    single<CoroutineScope> {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    factory { NotesRepositoryImp(
        notesLocalDataSource = get<NotesLocalDataSource>(),
        notesRemoteDataSource = get<NotesRemoteDataSource>()
    ) }.bind(NotesRepository::class)

    factory {
        NotesRemoteDataSourceImp(get<HttpClient>())
    }.bind(NotesRemoteDataSource::class)

    factory {
        NotesLocalDataSourceImp(
            get<NoteMarkDatabase>()
        )
    }.bind(NotesLocalDataSource::class)

    single<HttpClient> {
        HttpNetworkClientImp(get<NoteMarkPreferences>())
            .build()
    }
}
