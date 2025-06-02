package me.androidbox.designsystem.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import notemark.composeapp.generated.resources.Res

@Composable
fun NoteMarkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if(!darkTheme) lightColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(),
        shapes = Shapes,
        content = content
    )
}