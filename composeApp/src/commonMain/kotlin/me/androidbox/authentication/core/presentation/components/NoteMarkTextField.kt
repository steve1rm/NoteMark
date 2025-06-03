package me.androidbox.authentication.core.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteMarkTextField(
    label: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportText: String? = null,
    errorText: String? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val errorSupportColor = if (errorText == null) Color(0xff535364) else Color(0xffE1294B)
    val inputBorder = if (isFocused || errorText != null && !isFocused) {
        Modifier.border(
            1.dp, errorSupportColor,
            RoundedCornerShape(12.dp)
        )
    } else Modifier
    val inputBackground = if (isFocused || (!isFocused && errorText != null))
        Color.White
    else Color(0xffEFEFF2)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
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
            onValueChange = onValueChange,
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
            cursorBrush = SolidColor(errorSupportColor),
            singleLine = true,
            textStyle = TextStyle(
                color = Color(0xff1B1B1C),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            decorationBox = { input ->
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
        )

        if (isFocused && supportText != null && errorText == null) {
            Text(
                text = supportText,
                fontSize = 15.sp,
                color = Color(0xff535364)
            )
        }

        if (errorText != null) {
            Text(
                text = errorText,
                fontSize = 15.sp,
                color = Color(0xffE1294B)
            )
        }
    }
}