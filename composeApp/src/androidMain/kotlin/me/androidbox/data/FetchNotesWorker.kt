package me.androidbox.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.androidbox.core.models.DataError
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
                    when (results.value) {
                        DataError.Local.DISK_FULL -> Result.failure()
                        DataError.Local.EMPTY -> Result.failure()
                        DataError.Local.UNKNOWN -> Result.retry()
                        DataError.Network.BAD_REQUEST -> Result.retry()
                        DataError.Network.REQUEST_TIMEOUT -> Result.retry()
                        DataError.Network.METHOD_NOT_ALLOWED -> Result.failure()
                        DataError.Network.UNAUTHORIZED -> Result.retry()
                        DataError.Network.TOO_MANY_REQUESTS -> Result.retry()
                        DataError.Network.NO_INTERNET -> Result.retry()
                        DataError.Network.PAYLOAD_TOO_LARGE -> Result.failure()
                        DataError.Network.SERVER_ERROR -> Result.retry()
                        DataError.Network.SERIALIZATION -> Result.failure()
                        DataError.Network.JSON_CONVERT -> Result.failure()
                        DataError.Network.CONFLICT -> Result.failure()
                        DataError.Network.UNKNOWN -> Result.retry()
                    }
                }
            }
        }
    }
}