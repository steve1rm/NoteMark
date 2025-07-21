package me.androidbox

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import me.androidbox.core.data.NoteMarkDatabase
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.data.CreateNoteWorker
import me.androidbox.data.DeleteNoteWorker
import me.androidbox.data.FetchNotesWorker
import me.androidbox.data.SyncNoteWorkerScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    /** Workers */
    workerOf(::FetchNotesWorker)
    workerOf(::DeleteNoteWorker)
    workerOf(::CreateNoteWorker)

    singleOf(::SyncNoteWorkerScheduler).bind<SyncNoteScheduler>()
}