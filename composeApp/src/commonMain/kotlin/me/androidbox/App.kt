package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.startup.presentation.LandingScreen

@Composable
fun App() {
    NoteMarkTheme {
 //       RegisterScreen()
        LandingScreen()
    }
}