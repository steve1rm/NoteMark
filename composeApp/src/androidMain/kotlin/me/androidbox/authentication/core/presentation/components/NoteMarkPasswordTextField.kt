package me.androidbox.authentication.core.presentation.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.designsystem.theming.eye
import me.androidbox.designsystem.theming.eyeOff

@Composable
fun NoteMarkPasswordTextField(
    label: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    showPassword: Boolean,
    onToggleShowPassword : () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xff1B1B1C),
            fontWeight = FontWeight.Bold
        )
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation('\u002A'),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .then(
                    if (isFocused) Modifier.border(
                        1.dp,
                        Color(0xff535364),
                        RoundedCornerShape(12.dp)
                    )
                    else Modifier
                )
                .background(
                    if (isFocused)
                        Color.White
                    else Color(0xffEFEFF2)
                )
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            singleLine = true,
            textStyle = TextStyle(
                color = Color(0xff1B1B1C),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            decorationBox = { input ->
                Row {
                    val icon = if (showPassword) {
                        Icons.Outlined.eyeOff
                    } else Icons.Outlined.eye
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = Color(0xff535364),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        input.invoke()
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onToggleShowPassword)

                    )
                }
            }
        )
    }
}