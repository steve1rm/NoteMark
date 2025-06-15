package me.androidbox.notes.data.repository

import me.androidbox.core.models.DataError
import me.androidbox.notes.data.NoteDto
import me.androidbox.notes.data.NotesDto
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.domain.NotesRepository
import net.orandja.either.Either

class NotesRepositoryImp(
    private val notesRemoteDataSource: NotesRemoteDataSource
) : NotesRepository {
    override suspend fun createNote(noteDto: NoteDto): Either<NoteDto, DataError.Network> {
        return notesRemoteDataSource.createNote(noteDto)
    }

    override suspend fun updateNote(noteDto: NoteDto): Either<NoteDto, DataError.Network> {
        return notesRemoteDataSource.updateNote(noteDto)
    }

    override suspend fun deleteNote(id: String): Either<Unit, DataError.Network> {
        return notesRemoteDataSource.deleteNote(id)
    }

    override suspend fun fetchNotes(
        page: Int,
        size: Int
    ): Either<NotesDto, DataError.Network> {
        return notesRemoteDataSource.fetchNotes(page = page, size = size)
    }
}