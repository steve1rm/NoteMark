package me.androidbox.core.models

sealed interface Error

sealed interface DataError : Error {
    enum class Network : DataError {
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        JSONCONVERT,
        CONFLICT,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}