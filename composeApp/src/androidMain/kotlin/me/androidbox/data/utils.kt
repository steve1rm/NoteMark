package me.androidbox.data

import androidx.work.ListenableWorker
import me.androidbox.core.models.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when(this) {
        DataError.Local.DISK_FULL -> ListenableWorker.Result.failure()
        DataError.Local.EMPTY -> ListenableWorker.Result.failure()
        DataError.Local.UNKNOWN -> ListenableWorker.Result.retry()
        DataError.Network.BAD_REQUEST -> ListenableWorker.Result.retry()
        DataError.Network.REQUEST_TIMEOUT -> ListenableWorker.Result.retry()
        DataError.Network.METHOD_NOT_ALLOWED -> ListenableWorker.Result.failure()
        DataError.Network.UNAUTHORIZED -> ListenableWorker.Result.retry()
        DataError.Network.TOO_MANY_REQUESTS -> ListenableWorker.Result.retry()
        DataError.Network.NO_INTERNET -> ListenableWorker.Result.retry()
        DataError.Network.PAYLOAD_TOO_LARGE -> ListenableWorker.Result.failure()
        DataError.Network.SERVER_ERROR -> ListenableWorker.Result.retry()
        DataError.Network.SERIALIZATION -> ListenableWorker.Result.failure()
        DataError.Network.JSON_CONVERT -> ListenableWorker.Result.failure()
        DataError.Network.CONFLICT -> ListenableWorker.Result.failure()
        DataError.Network.UNKNOWN -> ListenableWorker.Result.retry()
    }
}
