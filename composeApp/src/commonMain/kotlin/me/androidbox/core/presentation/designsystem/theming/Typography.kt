package me.androidbox.core.presentation.designsystem.theming

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.inter
import notemark.composeapp.generated.resources.space_grotesk
import org.jetbrains.compose.resources.Font


internal val spaceGrotesk: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.space_grotesk, FontWeight.Normal)
    )

private val inter: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.inter, FontWeight.Normal)
    )


val Typography.headLineXSmall: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp,
            fontSize = 14.sp
        )
    }

@Composable
fun appTypography(): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 80.sp,
            fontSize = 66.sp
        ),
        displayMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 44.sp,
            fontSize = 40.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 48.sp,
            fontSize = 34.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 30.sp,
            fontSize = 26.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 26.sp,
            fontSize = 18.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            fontSize = 17.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            fontSize = 15.sp
        ),
        bodySmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            fontSize = 15.sp
        ),
        labelLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            fontSize = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            fontSize = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 18.sp,
            fontSize = 14.sp
        ),
        titleLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
            fontSize = 32.sp
        ),
        titleSmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            fontSize = 17.sp
        ),
        titleMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            fontSize = 20.sp
        ),
        displaySmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            fontSize = 16.sp
        )
    )
}