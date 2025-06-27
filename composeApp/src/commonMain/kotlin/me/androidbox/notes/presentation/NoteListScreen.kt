package me.androidbox.notes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.buttons.GradientFAB
import me.androidbox.core.presentation.designsystem.theming.bgGradient
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import me.androidbox.isTablet
import me.androidbox.notes.presentation.components.AvatarIcon
import me.androidbox.notes.presentation.components.DeleteNoteDialog
import me.androidbox.notes.presentation.components.NoteItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteListScreenRoot(
    onNavigateToEditNote: () -> Unit,
) {
    val viewModel = koinViewModel<NoteListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteListScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToEditNote = onNavigateToEditNote
    )
}

@Composable
fun NoteListScreen(
    state: NoteListUiState,
    onAction: (NoteListActions) -> Unit,
    onNavigateToEditNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenOrientation = getOrientation()
    val isTablet = isTablet()
    val gridColumns = if (screenOrientation == Orientation.PORTRAIT || isTablet)
        2 else if (screenOrientation == Orientation.LANDSCAPE) 3 else 2
    NoteMarkLayout(
        toolBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "NoteMark",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                AvatarIcon(
                    name = state.profilePicText,
                    backgroundColor = MaterialTheme.colorScheme.primary
                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(innerPadding)
                    .padding(all = 16.dp)
            ) {
                if (state.notesList.isNotEmpty()) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(gridColumns),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalItemSpacing = 16.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.notesList) { note ->
                            NoteItem(
                                noteItem = note,
                                onLongClick = {
                                    onAction(NoteListActions.OnShowDeleteDialog(note))
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "You’ve got an empty board,\n" +
                                    "let’s place your first note on it!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 80.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            GradientFAB(
                icon = Icons.Filled.Add,
                buttonGradient = MaterialTheme.colorScheme.bgGradient,
                onClick = onNavigateToEditNote

            )
        },
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .safeDrawingPadding()
    )

    if (state.showDeleteDialog) {
        DeleteNoteDialog(
            onCancel = {
                onAction(NoteListActions.OnCancelDeleteDialog)
            },
            onDeleteClick = {
                state.currentSelectedNote?.let {
                    onAction(NoteListActions.OnDeleteNote(it))
                }
            }
        )
    }
}