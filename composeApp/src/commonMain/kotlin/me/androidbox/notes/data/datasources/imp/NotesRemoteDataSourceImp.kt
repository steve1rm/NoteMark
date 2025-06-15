package me.androidbox.notes.data.datasources.imp

import me.androidbox.core.models.DataError
import me.androidbox.notes.data.NoteDto
import me.androidbox.notes.data.NotesDto
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import net.orandja.either.Either

class NotesRemoteDataSourceImp : NotesRemoteDataSource {
    override suspend fun createNote(): Either<NoteDto, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(): Either<NoteDto, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(id: String): Either<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchNotes(
        page: Int,
        size: Int
    ): Either<NotesDto, DataError.Network> {
        TODO("Not yet implemented")
    }
}