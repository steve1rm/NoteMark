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

    // FEEDBACK: If nothing is done with a variable assignment, return it directly
    override suspend fun createNote(noteItemDto: NoteItemDto): Either<NoteItemDto, DataError.Network> {
        val safeRequest = safeApiRequest<NoteItemDto> {
            val response = httpClient
                .post(Routes.NOTES) {
                    this.setBody(noteItemDto)
                }

            response
        }

        return safeRequest
    }

    override suspend fun updateNote(noteItemDto: NoteItemDto): Either<NoteItemDto, DataError.Network> {
        val safeRequest = safeApiRequest<NoteItemDto> {
            val response = httpClient
                .put(Routes.NOTES) {
                    this.setBody(noteItemDto)
                }

            response
        }

        return safeRequest
    }

    override suspend fun deleteNote(id: String): Either<Unit, DataError.Network> {
        val safeRequest = safeApiRequest<Unit>() {
            val response = httpClient
                .delete("${Routes.NOTES}/$id")

            response
        }

        return safeRequest
    }

    override suspend fun fetchNotes(
        page: Int,
        size: Int
    ): Either<NotesDto, DataError.Network> {
        val safeRequest = safeApiRequest<NotesDto>() {
            val response = httpClient
                .get(Routes.NOTES) {
                    this.parameter(PAGE, page)
                    this.parameter(SIZE, size)
                }

            response
        }

        return safeRequest
    }
}