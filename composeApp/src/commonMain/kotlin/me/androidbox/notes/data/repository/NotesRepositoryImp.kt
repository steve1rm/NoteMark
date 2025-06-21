package me.androidbox.notes.data.repository

import me.androidbox.core.models.DataError
import me.androidbox.notes.data.datasources.NotesLocalDataSource
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.mappers.toNoteItemEntity
import me.androidbox.notes.domain.model.NoteItem
import net.orandja.either.Either

class NotesRepositoryImp(
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val notesLocalDataSource: NotesLocalDataSource
) : NotesRepository {
    override suspend fun saveNote(noteItem: NoteItem): Either<Long, DataError.Local> {
        return notesLocalDataSource.saveNote(noteItem.toNoteItemEntity())
    }

    override suspend fun deleteNote(id: String): Either<Unit, DataError.Local> {
       // return notesRemoteDataSource.deleteNote(id)

        TODO()
    }

    override suspend fun fetchNotes(
        page: Int,
        size: Int
    ): Either<List<NoteItem>, DataError.Local> {
     //   return notesRemoteDataSource.fetchNotes(page = page, size = size)

        TODO()
    }
}