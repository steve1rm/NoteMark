package me.androidbox.user.domain

import me.androidbox.settings.presentation.model.SyncInterval

data class User(
    val userName: String,
    val syncInterval: SyncInterval,
    val syncTimeStamp: Long
)