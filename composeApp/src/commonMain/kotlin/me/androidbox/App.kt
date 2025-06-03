package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.startup.presentation.LandingLandscapeScreen
import me.androidbox.startup.presentation.LandingPortraitScreen

@Composable
fun App() {
    NoteMarkTheme {
        when(getOrientation()) {
            Orientation.PORTRAIT -> {
                LandingPortraitScreen()
            }
            Orientation.LANDSCAPE -> {
                LandingLandscapeScreen()
            }
        }
    }
}