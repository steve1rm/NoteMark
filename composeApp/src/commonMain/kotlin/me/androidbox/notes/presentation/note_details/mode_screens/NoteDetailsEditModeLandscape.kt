package me.androidbox.notes.presentation.note_details.mode_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.core.presentation.designsystem.buttons.TextButton
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.core.presentation.designsystem.theming.spaceGrotesk
import me.androidbox.notes.presentation.note_details.NoteDetailsActions
import me.androidbox.notes.presentation.note_details.NoteDetailsUiState

@Composable
fun NoteDetailsEditModeLandscape(
    state: NoteDetailsUiState,
    onAction: (NoteDetailsActions) -> Unit,
    modifier: Modifier = Modifier,
    noteId: String?,
) {
    NoteMarkTheme {
        Scaffold(
            modifier = modifier
                .fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(36.dp, Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = {
                        onAction(NoteDetailsActions.OnCloseClick)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasicTextField(
                        value = state.inputTitle,
                        onValueChange = {
                            onAction(NoteDetailsActions.OnTitleChange(it))
                        },
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        decorationBox = { input ->
                            if (state.inputTitle.isEmpty()) {
                                Text(
                                    text = "Note title",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            input.invoke()
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    )

                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                    BasicTextField(
                        value = state.inputContent,
                        onValueChange = {
                            onAction(NoteDetailsActions.OnContentChange(it))
                        },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        decorationBox = { input ->
                            if (state.inputContent.isEmpty()) {
                                Text(
                                    text = "Note content",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            input.invoke()
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }


                TextButton(
                    text = "SAVE NOTE",
                    onClick = {
                        onAction(NoteDetailsActions.OnSaveNote)
                    },
                    textStyle = TextStyle(
                        fontFamily = spaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}