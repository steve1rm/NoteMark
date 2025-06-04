package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    NoteMarkTheme {
        AppNavigation()
    }
}