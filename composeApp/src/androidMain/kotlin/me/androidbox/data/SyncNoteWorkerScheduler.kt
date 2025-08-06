package me.androidbox.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.androidbox.core.domain.SyncNoteScheduler
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncNoteWorkerScheduler(
    private val context: Context,
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
        val workRequest = PeriodicWorkRequestBuilder<SyncAllWorker>(interval.toJavaDuration())
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

        // FEEDBACK: Here, applicationScope.launch doesn't change anything
        applicationScope.launch {
            workManager.enqueue(workRequest)
        }.join()
    }
}