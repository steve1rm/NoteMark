package me.androidbox.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.data.models.NoteMarkPendingSyncDao
import net.orandja.either.Left
import net.orandja.either.Right

class DeleteNoteWorker(
    context: Context,
    private val workerParameters: WorkerParameters,
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val noteMarkPendingSyncDao: NoteMarkPendingSyncDao
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val NOTE_ID = "note_id"
    }

    override suspend fun doWork(): Result {
        if(runAttemptCount >= 5) {
            return Result.failure()
        }

        val noteId = workerParameters.inputData.getString(NOTE_ID) ?: return Result.failure()

        return when(val result = notesRemoteDataSource.deleteNote(noteId)) {
            is Left -> {
                noteMarkPendingSyncDao.deleteDeletedNoteMarkSyncEntity(noteId)
                Result.success()
            }
            is Right -> {
                result.value.toWorkerResult()
            }
        }
    }
}