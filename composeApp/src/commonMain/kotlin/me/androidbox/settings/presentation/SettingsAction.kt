package me.androidbox.settings.presentation

sealed interface SettingsAction {
    data object OnLogout : SettingsAction
}
