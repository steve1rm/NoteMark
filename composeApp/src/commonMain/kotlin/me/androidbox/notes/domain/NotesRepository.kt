package me.androidbox.notes.domain

import me.androidbox.core.models.DataError
import me.androidbox.notes.data.NoteDto
import me.androidbox.notes.data.NotesDto
import net.orandja.either.Either

interface NotesRepository {
    suspend fun createNote(noteDto: NoteDto): Either<NoteDto, DataError.Network>
    suspend fun updateNote(noteDto: NoteDto): Either<NoteDto, DataError.Network>
    suspend fun deleteNote(id: String): Either<Unit, DataError.Network>
    suspend fun fetchNotes(page: Int, size: Int): Either<NotesDto, DataError.Network>
}