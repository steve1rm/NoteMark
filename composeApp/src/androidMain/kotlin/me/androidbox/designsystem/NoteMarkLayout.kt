@file:OptIn(ExperimentalMaterial3Api::class)

package me.androidbox.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme

@Composable
fun NoteMarkLayout(
    modifier: Modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
    toolBar: @Composable () -> Unit = {},
    content: @Composable (paddingValue: PaddingValues) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    NoteMarkTheme {
        Scaffold(
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            topBar = {
                toolBar()

            },
            content = { paddingValues ->
                content(paddingValues)
            },
            bottomBar = {
                bottomBar()
            }
        )
    }
}