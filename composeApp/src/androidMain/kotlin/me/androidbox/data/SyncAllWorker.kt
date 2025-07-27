package me.androidbox.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.androidbox.notes.domain.NotesRepository

class SyncAllWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val notesRepository: NotesRepository
) : CoroutineWorker(
        context, workerParameters) {

    override suspend fun doWork(): Result {
        val result = if(runAttemptCount >= 3) {
            Result.failure()
        }
        else {
       //     notesRepository.syncPendingNotes()

            Result.success()
        }

        return result
    }
}