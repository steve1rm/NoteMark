package me.androidbox.core.presentation.designsystem.theming

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ColorScheme.bgGradient: Brush
    get() = Brush.linearGradient(
        listOf(
            Color(0xff58A1F8),
            Color(0xff5A4CF7)
        )
    )

val lightPrimary = Color(0xff5977F7)
val lightSecondary = Color(0x5977F71A)
val error = Color(0xffE1294B)
val lightOnPrimary = Color(0xffffffff)
val lightOnSurface = Color(0xff1B1B1C)
val lightOnSurfaceVariant = Color(0xff535364)
val lightSurface = Color(0xffEFEFF2)

internal val lightColorScheme = lightColorScheme(
    primary = lightPrimary,
    secondary = lightSecondary,
    onPrimary = lightOnPrimary,
    onSurface = lightOnSurface,
    onSurfaceVariant = lightOnSurfaceVariant,
    surface = lightSurface,
    error = error
)