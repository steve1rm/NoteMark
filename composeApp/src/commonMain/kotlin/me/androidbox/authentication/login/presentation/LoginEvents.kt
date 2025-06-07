package me.androidbox.authentication.login.presentation

import me.androidbox.core.models.DataError

interface LoginEvents {

    data object OnLoginSuccess: LoginEvents
    data class OnLoginFail(val dataError: DataError) : LoginEvents
}