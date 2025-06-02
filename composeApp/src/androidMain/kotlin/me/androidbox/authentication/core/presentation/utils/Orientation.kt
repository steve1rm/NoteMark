package me.androidbox.authentication.core.presentation.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import me.androidbox.authentication.core.presentation.models.Orientation

@Composable
fun getOrientation(): Orientation {
    val configuration = LocalConfiguration.current

    return when(configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Orientation.PORTRAIT
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            Orientation.LANDSCAPE
        }
        else -> {
            Orientation.PORTRAIT
        }
    }
}