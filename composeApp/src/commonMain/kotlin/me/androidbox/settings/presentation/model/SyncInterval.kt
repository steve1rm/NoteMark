package me.androidbox.settings.presentation.model

enum class SyncInterval(
    val text: String
) {
    MANUAL("Manual only"),
    MINUTES_15("15 minutes"),
    MINUTES_30("30 minutes"),
    MINUTES_60("1 hour"),
}