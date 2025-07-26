package me.androidbox.core.domain

import me.androidbox.notes.domain.model.NoteItem
import kotlin.time.Duration

interface SyncNoteScheduler {

    suspend fun scheduleSync(syncTypes: SyncTypes)
    suspend fun cancelAllSyncs()

    sealed interface SyncTypes {
        data class FetchNotes(val interval: Duration) : SyncTypes
        data class DeleteNote(val noteId: String) : SyncTypes
        class CreateNote(val noteItem: NoteItem) : SyncTypes
    }
}