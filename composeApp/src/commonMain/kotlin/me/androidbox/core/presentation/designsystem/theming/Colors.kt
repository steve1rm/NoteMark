package me.androidbox.core.presentation.designsystem.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ColorScheme.onBackgroundVariant: Color
    @Composable
    get() {
        return if(isSystemInDarkTheme()) {
            Color(0xff7F7163)
        }
        else {
            Color(0xff7F7163)
        }
    }

val ColorScheme.pink: Color
    @Composable
    get() {
        return if(isSystemInDarkTheme()) {
            Color(0xffED6363)
        }
        else {
            Color(0xffED6363)
        }
    }

val ColorScheme.success: Color
    @Composable
    get() {
        return if(isSystemInDarkTheme()) {
            Color(0xff0DD280)
        }
        else {
            Color(0xff0DD280)
        }
    }

val ColorScheme.bgGradient : Brush
    get() = Brush.linearGradient(listOf(
        Color(0xff58A1F8),
        Color(0xff5A4CF7)
    ))

val lightPrimary = Color(0xff238CFF)
val lightSecondary = Color(0xffAB5CFA)
val lightTertiary = Color(0xffFA852C)
val error = Color(0xffEF1242)
val lightOnPrimary = Color(0xffffffff)
val lightBackground = Color(0xffFEFAF6)
val lightOnBackground = Color(0xff514437)

val lightOnSurface = Color(0xffA5978A)
val lightOnSurfaceVariant = Color(0xffF6F1EC)
val lightSurfaceLow = Color(0xffEEE7E0)
val lightSurfaceLowest = Color(0xffE1D5CA)
val lightSurfaceHigh = Color(0xffffffff)

internal val lightColorScheme = lightColorScheme(
    primary = lightPrimary,
    onPrimary = lightOnPrimary,
    background = lightBackground,
    onBackground = lightOnBackground,
    onSurface = lightOnSurface,
    secondary = lightSecondary,
    surfaceContainerHigh = lightSurfaceHigh,
    surfaceContainerLowest = lightSurfaceLowest,
    onSurfaceVariant = lightOnSurfaceVariant,
    surfaceContainerLow = lightSurfaceLow,
    tertiary = lightTertiary,
    error = error,
)

internal val darkColorScheme = lightColorScheme(
    primary = lightPrimary,
    background = lightBackground,
    onBackground = lightOnBackground,
    onSurface = lightOnSurface,
    secondary = lightSecondary,
)