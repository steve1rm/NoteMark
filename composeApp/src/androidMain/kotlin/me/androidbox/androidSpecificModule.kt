package me.androidbox

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidSpecificModule = module {
    single<NoteMarkPreferences> {
        NoteMarkPreferencesImp(
            context = androidContext()
        )
    }
}