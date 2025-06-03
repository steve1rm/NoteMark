package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.authentication.register.presentation.RegisterScreen
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme

@Composable
fun App() {
    NoteMarkTheme {
        RegisterScreen()
    }
}