package me.androidbox.notes.presentation.note_details.mode_screens

import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.notes.presentation.note_details.NoteDetailsActions
import me.androidbox.notes.presentation.note_details.NoteDetailsUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NoteDetailsViewerModeLandscape(
    state: NoteDetailsUiState,
    onAction: (NoteDetailsActions) -> Unit,
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState,
) {
    NoteMarkLayout(
        content = { innerPadding ->
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(onClick = {
                                    onAction(NoteDetailsActions.OnCloseClick)
                                }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Go back icon"
                            )

                            Text(
                                text = "All notes".uppercase(),
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(
                            modifier = Modifier
                                .width(540.dp)
                                .align(Alignment.Center),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = state.noteTitle,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )

                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Date created",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = state.noteCreatedDateFormatted,
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Last edited",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = state.noteEditedDateFormatted,
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )

                            Text(
                                text = state.noteContent,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        snackState = snackState
    )
}

@Preview
@Composable
fun NoteDetailsViewerModeLandscapePreview() {
    NoteMarkTheme {
        NoteDetailsViewerModeLandscape(
            state = NoteDetailsUiState(),
            onAction = {},
            snackState = SnackbarHostState()
        )
    }
}