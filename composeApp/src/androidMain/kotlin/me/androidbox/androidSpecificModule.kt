package me.androidbox

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import me.androidbox.core.data.NoteMarkDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidSpecificModule = module {
    single<NoteMarkPreferences> {
        NoteMarkPreferencesImp(
            context = androidContext()
        )
    }

    single<ConnectivityManager> {
        AndroidConnectivityManager(
            context = androidContext()
        )
    }

    single<NoteMarkDatabase> {
        val dbFile = androidContext().getDatabasePath("notemark.db")

        Room.databaseBuilder<NoteMarkDatabase>(
            context = androidContext().applicationContext,
            name = dbFile.absolutePath
        )
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}