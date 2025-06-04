package me.androidbox.core.data

object Routes {
    private const val BASE_URL = "https://notemark.pl-coding.com/"
    const val REGISTATION = "$BASE_URL/api/auth/register"
    const val LOGIN = "$BASE_URL/api/auth/login"
    const val TOKEN_REFRESH = "$BASE_URL/api/auth/refresh"

}