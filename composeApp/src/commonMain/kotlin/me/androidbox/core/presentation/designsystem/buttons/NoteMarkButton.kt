package me.androidbox.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteMarkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff5977F7),
            contentColor = Color.White,
            disabledContainerColor = Color(0x1F1B1C1F),
            disabledContentColor = Color(0xff1B1B1C)
        ),
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(4.dp),
            fontSize = 17.sp
        )
    }
}