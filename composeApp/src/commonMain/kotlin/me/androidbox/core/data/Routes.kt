package me.androidbox.core.data

object Routes {
    private const val BASE_URL = "https://notemark.pl-coding.com"
    const val REGISTRATION = "$BASE_URL/api/auth/register"
    const val LOGIN = "$BASE_URL/api/auth/login"
    const val NOTES = "$BASE_URL/api/notes"
    const val DELETE_NOTE = "$BASE_URL/api/notes/{id}"
    const val TOKEN_REFRESH = "$BASE_URL/api/auth/refresh"

}