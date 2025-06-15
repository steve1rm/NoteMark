package me.androidbox.core.models

sealed interface Error

sealed interface DataError : Error {
    enum class Network(val errorMessage: String) : DataError {
        BAD_REQUEST("Returned when something about your request is not in the right format"),
        REQUEST_TIMEOUT(""),
        METHOD_NOT_ALLOWED("This means you’re using the wrong HTTP method, for example a POST request to an endpoint that expected a GET request."),
        UNAUTHORIZED("Invalid login credentials"),
        TOO_MANY_REQUESTS("You’ve hit the rate limit of 400 requests per hour. Simply wait a bit and try again"),
        NO_INTERNET(""),
        PAYLOAD_TOO_LARGE(""),
        SERVER_ERROR(""),
        SERIALIZATION(""),
        JSON_CONVERT(""),
        CONFLICT("Your request itself was valid, but it led to a server-side conflict. This could be returned for example if you try to register with an email that already exists or if you try to insert an item with an ID that already exists"),
        UNKNOWN("")
    }

    enum class Local : DataError {
        DISK_FULL
    }
}