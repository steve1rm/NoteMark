package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.authentication.core.presentation.models.Orientation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun getOrientation(): Orientation

@Composable
expect fun isAtLeastMedium(): Boolean
