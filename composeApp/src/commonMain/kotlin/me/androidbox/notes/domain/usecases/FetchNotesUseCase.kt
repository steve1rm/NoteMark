package me.androidbox.notes.domain.usecases

import kotlinx.coroutines.flow.Flow
import me.androidbox.core.models.DataError
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

fun interface FetchNotesUseCase {
    suspend fun execute(): Flow<List<NoteItem>>
}