package me.androidbox

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.logger.Level

class NoteMarkApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeKoin(
            config = {
                androidContext(this@NoteMarkApplication)
                androidLogger(Level.DEBUG)
                workManagerFactory()
          // FIXME      analytics { apiKey() }
            },
            androidSpecificModule
        )
    }
}