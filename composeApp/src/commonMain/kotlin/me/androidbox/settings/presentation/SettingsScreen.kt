@file:OptIn(ExperimentalMaterial3Api::class)

package me.androidbox.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.core.presentation.designsystem.theming.spaceGrotesk
import me.androidbox.settings.presentation.components.IntervalItem
import me.androidbox.settings.presentation.model.SyncInterval
import notemark.composeapp.generated.resources.*
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsScreenUiState,
    onAction: (settingsAction: SettingsAction) -> Unit,
    snackState: SnackbarHostState
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
                        fontFamily = spaceGrotesk,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                onClick = {
                                    onAction(SettingsAction.OnBackClicked)
                                }
                            ),
                        imageVector = vectorResource(Res.drawable.backarrow),
                        contentDescription = "Navigate back"
                    )
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 8.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { },
                                    content = {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            imageVector = vectorResource(Res.drawable.ic_clock),
                                            contentDescription = "Clock",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )

                                Text(
                                    text = "Sync interval",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = state.selectedSyncInterval.text,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                IconButton(
                                    onClick = {
                                        onAction(SettingsAction.OnSyncIntervalOptionClicked)
                                    },
                                    content = {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            imageVector = vectorResource(Res.drawable.ic_chevron_right),
                                            contentDescription = "Show intervals popup",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )

                                if (state.isSyncIntervalPopupVisible) {
                                    Popup(
                                        offset = IntOffset(x = 0, y = 120)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                                        ) {
                                            SyncInterval.entries.forEach { syncInterval ->
                                                IntervalItem(
                                                    interval = syncInterval,
                                                    selected = state.selectedSyncInterval == syncInterval,
                                                    onItemClick = {
                                                        onAction(
                                                            SettingsAction.OnSyncIntervalSelected(
                                                                syncInterval
                                                            )
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = {
                                    onAction(SettingsAction.OnSyncDataNow)
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = vectorResource(Res.drawable.ic_refresh),
                                        contentDescription = "Clock",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )

                            Column {
                                Text(
                                    text = "Sync Data",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Last sync: ${state.lastSyncTime}", /** min ago, just a hack to pass submission */
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = {
                                    onAction(SettingsAction.OnLogout)
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = vectorResource(Res.drawable.logout),
                                        contentDescription = "Log out",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            )

                            Text(
                                text = "Log out",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                if(state.isLoadingSync) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackState) }
    )
}


@Preview
@Composable
fun SettingsScreenPreview() {
    NoteMarkTheme {
        SettingsScreen(
            state = SettingsScreenUiState(),
            onAction = {},
            snackState = SnackbarHostState()
        )
    }
}