package me.androidbox.data

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.androidbox.authentication.register.domain.use_case.FetchUserByUserNameUseCaseImp
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.notes.data.models.NoteMarkPendingSyncDao
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class SyncNoteWorkerScheduler(
    private val context: Context,
    private val noteMarkPendingSyncDao: NoteMarkPendingSyncDao,
    private val fetchUserByUserNameUseCaseImp: FetchUserByUserNameUseCaseImp,
    private val applicationScope: CoroutineScope
) : SyncNoteScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncTypes: SyncNoteScheduler.SyncTypes) {
        if(syncTypes is SyncNoteScheduler.SyncTypes.SyncAll) {
            scheduleSyncAllWorker(syncTypes.interval)
        }
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    private suspend fun scheduleSyncAllWorker(interval: Duration) {
        val workRequest = OneTimeWorkRequestBuilder<SyncAllWorker>()
            .addTag("syncAll_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffDelay = 2_000L,
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest)
        }.join()
    }
}