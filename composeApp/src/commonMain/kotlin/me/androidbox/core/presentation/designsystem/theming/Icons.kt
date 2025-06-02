package me.androidbox.core.presentation.designsystem.theming

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.ic_eye
import notemark.composeapp.generated.resources.ic_eye_off
import org.jetbrains.compose.resources.vectorResource

val eye: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_eye)

val eyeOff: ImageVector
    @Composable
    get() = vectorResource(Res.drawable.ic_eye_off)