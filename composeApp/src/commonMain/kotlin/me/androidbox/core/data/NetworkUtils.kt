package me.androidbox.core.data

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import me.androidbox.core.models.DataError
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

suspend inline fun <reified D> safeApiRequest(block: () -> HttpResponse) : Either<D, DataError.Network> {
    val httpResponse = try {
        block()
    }
    catch(exception: UnresolvedAddressException) {
        exception.printStackTrace()

        return Right(DataError.Network.NO_INTERNET)
    }
    catch (exception: SerializationException) {
        exception.printStackTrace()

        return Right(DataError.Network.SERIALIZATION)
    }
    catch (exception: Exception) {
        if(exception is CancellationException) {
            throw exception
        }

        return Right(DataError.Network.UNKNOWN)
    }

    return responseToResult(httpResponse)
}

suspend inline fun <reified D> responseToResult(response: HttpResponse): Either<D, DataError.Network> {
    val either = when(response.status.value) {
        in 200..299 -> {
            Left(response.body<D>())
        }
        401 -> {
            Right(DataError.Network.UNAUTHORIZED)
        }
        408 -> {
            Right(DataError.Network.REQUEST_TIMEOUT)
        }
        409 -> {
            Right(DataError.Network.CONFLICT)
        }
        413 -> {
            Right(DataError.Network.PAYLOAD_TOO_LARGE)
        }
        429 -> {
            Right(DataError.Network.TOO_MANY_REQUESTS)
        }
        in 500..599 -> {
            Right(DataError.Network.SERVER_ERROR)
        }
        else -> {
            Right(DataError.Network.UNKNOWN)
        }
    }

    return either
}