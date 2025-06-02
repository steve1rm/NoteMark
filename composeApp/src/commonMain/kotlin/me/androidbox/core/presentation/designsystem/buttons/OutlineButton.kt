package me.androidbox.core.presentation.designsystem.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OutlineButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.height(48.dp),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun OutlineButtonPreview() {
    NoteMarkTheme {
        OutlineButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Login",
            enabled = true,
            onClick = {}
        )
    }
}
