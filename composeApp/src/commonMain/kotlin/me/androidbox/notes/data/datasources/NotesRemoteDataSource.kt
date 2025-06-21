package me.androidbox.notes.data.datasources

import me.androidbox.core.models.DataError
import me.androidbox.notes.data.models.NoteItemDto
import me.androidbox.notes.data.models.NotesDto
import net.orandja.either.Either

interface NotesRemoteDataSource {
    suspend fun createNote(noteItemDto: NoteItemDto): Either<NoteItemDto, DataError.Network>
    suspend fun updateNote(noteItemDto: NoteItemDto): Either<NoteItemDto, DataError.Network>
    suspend fun deleteNote(id: String): Either<Unit, DataError.Network>
    suspend fun fetchNotes(page: Int, size: Int): Either<NotesDto, DataError.Network>
}