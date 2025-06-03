package me.androidbox.startup.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.startup.presentation.components.LandingContent
import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.landingbackground
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LandingPortraitScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(resource = Res.drawable.landingbackground),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

        LandingContent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ),
            onLoginClicked = {},
            onGettingStartedClicked = {}
        )
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    NoteMarkTheme {
        LandingPortraitScreen()
    }
}