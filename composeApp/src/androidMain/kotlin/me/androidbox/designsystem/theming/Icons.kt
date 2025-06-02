package me.androidbox.designsystem.theming

import me.androidbox.R
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

val Icons.Outlined.eye: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_eye)

val Icons.Outlined.eyeOff: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_eye_off)
