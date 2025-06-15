package me.androidbox.notes.data

import me.androidbox.core.models.DataError
import net.orandja.either.Either

interface NotesRemoteDataSource {
    suspend fun createNote(): Either<NoteDto, DataError.Network>
    suspend fun updateNote(): Either<NoteDto, DataError.Network>
    suspend fun deleteNote(id: String): Either<Unit, DataError.Network>
    suspend fun fetchNotes(page: Int, size: Int): Either<NotesDto, DataError.Network>
}