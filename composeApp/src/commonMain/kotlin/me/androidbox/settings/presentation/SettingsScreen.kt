@file:OptIn(ExperimentalMaterial3Api::class)

package me.androidbox.settings.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.core.presentation.designsystem.theming.spaceGrotesk
import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.backarrow
import notemark.composeapp.generated.resources.logout
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onAction: (settingsAction: SettingsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings".uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        fontFamily = spaceGrotesk
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = vectorResource(Res.drawable.backarrow),
                        contentDescription = "Navigate back"
                    )
                }
            )
        },
        content = { paddingValues ->
            Row(
                modifier = modifier.padding(paddingValues),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onAction(SettingsAction.OnLogout)
                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = vectorResource(Res.drawable.logout),
                            contentDescription = "Logout"
                        )
                    }
                )

                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.titleSmall)
            }
        }
    )
}

@Preview
@Composable
fun SettingsScreenPreview() {
    NoteMarkTheme {
        SettingsScreen(
            onAction = {}
        )
    }
}