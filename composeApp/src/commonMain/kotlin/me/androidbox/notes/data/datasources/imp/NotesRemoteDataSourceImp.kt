package me.androidbox.notes.data.datasources.imp

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import me.androidbox.core.data.Routes
import me.androidbox.core.data.Routes.PAGE
import me.androidbox.core.data.Routes.SIZE
import me.androidbox.core.data.models.DataError
import me.androidbox.core.data.safeApiRequest
import me.androidbox.notes.data.datasources.NotesRemoteDataSource
import me.androidbox.notes.data.models.NoteItemDto
import me.androidbox.notes.data.models.NotesDto
import net.orandja.either.Either

class NotesRemoteDataSourceImp(
    private val httpClient: HttpClient
) : NotesRemoteDataSource {

    override suspend fun createNote(noteItemDto: NoteItemDto): Either<NoteItemDto, DataError.Network> {
        return safeApiRequest<NoteItemDto> {
            httpClient
                .post(Routes.NOTES) {
                    this.setBody(noteItemDto)
                }
        }
    }

    override suspend fun updateNote(noteItemDto: NoteItemDto): Either<NoteItemDto, DataError.Network> {
        return safeApiRequest<NoteItemDto> {
            httpClient
                .put(Routes.NOTES) {
                    this.setBody(noteItemDto)
                }
        }
    }

    override suspend fun deleteNote(id: String): Either<Unit, DataError.Network> {
        return safeApiRequest<Unit>() {
            httpClient
                .delete("${Routes.NOTES}/$id")
        }
    }

    override suspend fun fetchNotes(
        page: Int,
        size: Int
    ): Either<NotesDto, DataError.Network> {
        return safeApiRequest<NotesDto>() {
            httpClient
                .get(Routes.NOTES) {
                    this.parameter(PAGE, page)
                    this.parameter(SIZE, size)
                }
        }
    }
}