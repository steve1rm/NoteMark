package me.androidbox.core.presentation.designsystem.textfields

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.core.models.Constants.starChar
import me.androidbox.core.presentation.designsystem.theming.eye
import me.androidbox.core.presentation.designsystem.theming.eyeOff

@Composable
fun NoteMarkPasswordTextField(
    label: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    showPassword: Boolean,
    onToggleShowPassword: () -> Unit,
    modifier: Modifier = Modifier,
    supportText: String? = null,
    errorText: String? = null,
) {
    var isFocused by remember { mutableStateOf(false) }

    val errorSupportColor = if (errorText == null) MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.colorScheme.error

    val inputBorder = if (isFocused || errorText != null && !isFocused) {
        Modifier.border(
            1.dp, errorSupportColor,
            RoundedCornerShape(12.dp)
        )
    } else Modifier

    val inputBackground = if (isFocused || (!isFocused && errorText != null))
        Color.White
    else MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(starChar),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .then(inputBorder)
                .background(inputBackground)
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            decorationBox = { input ->
                Row {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        input.invoke()
                    }
                    Icon(
                        imageVector = if (showPassword) eye else eyeOff,
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onToggleShowPassword)

                    )
                }
            }
        )

        if (isFocused && supportText != null && errorText == null) {
            Text(
                text = supportText,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (errorText != null) {
            Text(
                text = errorText,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}