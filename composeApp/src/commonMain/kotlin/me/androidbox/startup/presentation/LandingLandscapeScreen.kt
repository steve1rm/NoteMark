package me.androidbox.startup.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.startup.presentation.components.LandingContent
import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.landscapebackground
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LandingLandscapeScreen(
    modifier: Modifier = Modifier,
    onGettingStartedClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xffE0EAFF)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.weight(1f),
                alignment = Alignment.CenterStart,
                painter = painterResource(resource = Res.drawable.landscapebackground),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            LandingContent(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    ),
                onGettingStartedClicked = onGettingStartedClick,
                onLoginClicked = onLoginClick
            )
        }
    }
}

@Preview
@Composable
fun LandingLandscapeScreenPreview() {
    NoteMarkTheme {
        LandingLandscapeScreen(
            Modifier,
            {},
            {}
        )
    }
}