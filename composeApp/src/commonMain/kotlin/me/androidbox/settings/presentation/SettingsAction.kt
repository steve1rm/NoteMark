package me.androidbox.settings.presentation

import me.androidbox.settings.presentation.model.SyncInterval

sealed interface SettingsAction {
    data object OnLogout : SettingsAction
    data object OnSyncIntervalOptionClicked : SettingsAction
    data class OnSyncIntervalSelected(val syncInterval: SyncInterval) : SettingsAction
}
