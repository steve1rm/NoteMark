package me.androidbox.core.domain

import kotlin.time.Duration

interface SyncNoteScheduler {

    suspend fun scheduleSync(syncTypes: SyncTypes)
    suspend fun cancelAllSyncs()

    sealed interface SyncTypes {
        data class SyncAll(val interval: Duration) : SyncTypes
    }
}