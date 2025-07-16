package me.androidbox.settings.presentation

import me.androidbox.settings.presentation.model.SyncInterval

data class SettingsScreenUiState(
    val isSyncIntervalPopupVisible: Boolean = false,
    val selectedSyncInterval: SyncInterval = SyncInterval.MANUAL,
    val lastSyncTime: String = "12"
)
