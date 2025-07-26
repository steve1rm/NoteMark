package me.androidbox.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.androidbox.notes.domain.NotesRepository
import net.orandja.either.Left
import net.orandja.either.Right

class FetchNotesWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val notesRepository: NotesRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if(runAttemptCount >= 5) {
            return Result.failure()
        }
        else {
            val results = notesRepository.fetchAllNotes()

            return when (results) {
                is Left -> {
                    Result.success()
                }

                is Right -> {
                    results.value.toWorkerResult()
                }
            }
        }
    }
}