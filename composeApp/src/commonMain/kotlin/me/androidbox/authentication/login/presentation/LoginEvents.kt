package me.androidbox.authentication.login.presentation

interface LoginEvents {

    data object OnLoginSuccess: LoginEvents
    data object OnLoginFail : LoginEvents
}