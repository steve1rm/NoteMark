package me.androidbox.startup.presentation

import androidx.compose.runtime.Composable
import me.androidbox.core.models.Orientation
import me.androidbox.getOrientation

@Composable
fun LandingScreen(
    onGettingStartedClick : () -> Unit,
    onLoginClick : () -> Unit,
) {
    when(getOrientation()) {
        Orientation.PORTRAIT -> {
            LandingPortraitScreen(
                onGettingStartedClick = onGettingStartedClick,
                onLoginClick = onLoginClick,
            )
        }
        Orientation.LANDSCAPE -> {
            LandingLandscapeScreen(
                onGettingStartedClick = onGettingStartedClick,
                onLoginClick = onLoginClick,
            )
        }
    }
}