package me.androidbox.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.notes.data.mappers.toNoteItemEntity
import me.androidbox.notes.data.models.DeletedNoteMarkSyncEntity
import me.androidbox.notes.data.models.NoteMarkPendingSyncDao
import me.androidbox.notes.data.models.NoteMarkPendingSyncEntity
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.user.data.UserLocalDataSource
import net.orandja.either.Left
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncNoteWorkerScheduler(
    private val context: Context,
    private val noteMarkPendingSyncDao: NoteMarkPendingSyncDao,
    private val userLocalDataSource: UserLocalDataSource,
    private val applicationScope: CoroutineScope
) : SyncNoteScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncTypes: SyncNoteScheduler.SyncTypes) {
        when(syncTypes) {
            is SyncNoteScheduler.SyncTypes.FetchNotes -> {
                scheduleFetchNotesWorker(syncTypes.interval)
            }

            is SyncNoteScheduler.SyncTypes.DeleteNote -> {
                scheduleDeleteRunWorker(syncTypes.noteId)
            }

            is SyncNoteScheduler.SyncTypes.CreateNote -> {
                scheduleCreateNoteWorker(syncTypes.noteItem)
            }
        }
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    private suspend fun scheduleDeleteRunWorker(noteId: String) {
        val result = userLocalDataSource.fetchUser()

        if(result is Left) {
            val entity = DeletedNoteMarkSyncEntity(
                id = noteId,
                userName = result.left.userName
            )

            noteMarkPendingSyncDao.upsertDeletedNoteMarkEntity(
                deletedNoteMarkSyncEntity = entity
            )

            val workRequest = OneTimeWorkRequestBuilder<DeleteNoteWorker>()
                .addTag("delete_work")
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.EXPONENTIAL,
                    backoffDelay = 2_000L,
                    timeUnit = TimeUnit.MILLISECONDS
                )
                .setInputData(
                    Data.Builder()
                        .putString(DeleteNoteWorker.NOTE_ID, entity.id)
                        .build()
                )
                .build()

            applicationScope.launch {
                workManager.enqueue(workRequest)
            }.join()
        }
    }

    private suspend fun scheduleCreateNoteWorker(noteItem: NoteItem) {
        val result = userLocalDataSource.fetchUser()

        if(result is Left) {
            val pendingNote = NoteMarkPendingSyncEntity(
                noteMark = noteItem.toNoteItemEntity(),
                userName = result.left.userName
            )

            noteMarkPendingSyncDao.upsertNoteMarkPendingSyncEntity(pendingNote)

            val workRequest = OneTimeWorkRequestBuilder<CreateNoteWorker>()
                .addTag("create_work")
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.EXPONENTIAL,
                    backoffDelay = 2_000L,
                    timeUnit = TimeUnit.MILLISECONDS
                )
                .setInputData(
                    Data.Builder()
                        .putString(CreateNoteWorker.NOTE_ID, pendingNote.noteMarkId)
                        .build()
                )
                .build()

            applicationScope.launch {
                workManager.enqueue(workRequest)
            }.join()
        }
    }

    private suspend fun scheduleFetchNotesWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager
                .getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }

        if(!isSyncScheduled) {
            val workRequest = PeriodicWorkRequestBuilder<FetchNotesWorker>(
                repeatInterval = interval.toJavaDuration()
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.EXPONENTIAL,
                    backoffDelay = 2_000L,
                    timeUnit = TimeUnit.MILLISECONDS
                )
                .setInitialDelay(
                    duration = 30,
                    timeUnit = TimeUnit.MINUTES
                )
                .addTag("sync_work")
                .build()

            workManager
                .enqueue(workRequest)
                .await()
        }
    }
}