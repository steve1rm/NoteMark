package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.navigation.AppNavigation
import me.androidbox.navigation.TestNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    NoteMarkTheme {
        TestNavigation()
    }
}