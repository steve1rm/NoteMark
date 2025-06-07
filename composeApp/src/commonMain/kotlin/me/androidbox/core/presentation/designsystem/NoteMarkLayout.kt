@file:OptIn(ExperimentalMaterial3Api::class)

package me.androidbox.core.presentation.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NoteMarkLayout(
    modifier: Modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
    toolBar: @Composable () -> Unit = {},
    content: @Composable (paddingValue: PaddingValues) -> Unit,
    bottomBar: @Composable () -> Unit = {},
    snackState: SnackbarHostState? = null
) {
    Scaffold(
        snackbarHost = { snackState?.let { SnackbarHost(it) } },
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