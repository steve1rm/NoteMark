package me.androidbox.startup.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.designsystem.buttons.OutlineButton
import me.androidbox.core.presentation.designsystem.buttons.SolidButton
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LandingContent(
    modifier: Modifier = Modifier,
    onGettingStartedClicked: () -> Unit,
    onLoginClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {

        Text(
            text = "Your Own Collection of Notes",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Capture your thoughts and ideas.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        SolidButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Get Started",
            onClick = onGettingStartedClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlineButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Log in",
            onClick = onLoginClicked
        )
    }
}

@Preview
@Composable
fun LandingContentPreview() {
    NoteMarkTheme {
        LandingContent(
            onGettingStartedClicked = {},
            onLoginClicked = {}
        )
    }
}