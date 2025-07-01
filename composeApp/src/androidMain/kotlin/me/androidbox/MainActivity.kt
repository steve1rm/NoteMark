package me.androidbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.navigation.AppNavigation
import me.androidbox.notes.presentation.edit_note.EditNoteScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.showSplash
            }
        }

        enableEdgeToEdge()

        setContent {
            AppNavigation()
        }

    }
}

@PreviewScreenSizes
@Composable
fun AppAndroidPreview() {
    NoteMarkTheme {
        EditNoteScreenRoot(
            "", {}
        )
    }
}