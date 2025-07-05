package me.androidbox.settings.presentation

sealed interface SettingsEvent {
    data class logoutSuccess(val isSuccess: Boolean) : SettingsEvent
}