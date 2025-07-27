package me.androidbox.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.androidbox.notes.domain.NotesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncAllWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters), KoinComponent {
    private val notesRepository: NotesRepository by inject()

    override suspend fun doWork(): Result {
        val result = if(runAttemptCount >= 3) {
            Result.failure()
        }
        else {
            notesRepository.syncPendingNotes()

            Result.success()
        }

        return result
    }
}