package me.androidbox.notes.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.buttons.GradientFAB
import me.androidbox.core.presentation.designsystem.theming.bgGradient
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.core.presentation.utils.Previews.noteItem
import me.androidbox.getOrientation
import me.androidbox.isTablet
import me.androidbox.notes.presentation.components.AvatarIcon
import me.androidbox.notes.presentation.components.DeleteNoteDialog
import me.androidbox.notes.presentation.components.NoteItem
import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteListScreenRoot(
    username: String,
    onNavigateToEditNote: (noteId: String?) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val viewModel = koinViewModel<NoteListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is NoteListEvents.OnNavigateToEditNote -> {
                onNavigateToEditNote(event.noteId)
            }

            is NoteListEvents.OnFailureMessage -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    NoteListScreen(
        username = username,
        state = state,
        onAction = { noteListActions ->
            when(noteListActions) {
                NoteListActions.OnSettingsClicked -> {
                    onNavigateToSettings()
                }
                else -> {
                    viewModel.onAction(noteListActions)
                }
            }
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun NoteListScreen(
    username: String,
    state: NoteListUiState,
    onAction: (NoteListActions) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
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

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        onAction(NoteListActions.OnSettingsClicked)
                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = vectorResource(Res.drawable.settings),
                            contentDescription = "Open settings")
                    }
                )

                AvatarIcon(
                    name = username,
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
                        items(items = state.notesList,
                            key = { noteItem ->
                                noteItem.id
                            }) { note ->
                            println("items in lazyGrid ${note.id} | ${note.title}")
                            NoteItem(
                                noteItem = note,
                                onClick = {
                                    onAction(NoteListActions.OnNavigateToEditNoteWithNoteId(note.id))
                                },
                                onLongClick = { noteToDelete ->
                                    println("onLongClick with ${note.id} | ${note.title}")
                                    onAction(NoteListActions.OnShowDeleteDialog(noteToDelete))
                                },
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
                onClick = {
                    onAction(NoteListActions.OnNavigateToEditNoteWithNewNote)
                }

            )
        },
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .safeDrawingPadding(),
        snackState = snackbarHostState
    )

    if (state.showDeleteDialog) {
        DeleteNoteDialog(
            onCancel = {
                onAction(NoteListActions.OnCancelDeleteDialog)
            },
            onDeleteClick = {
                state.currentSelectedNote?.let { noteItem ->
                    onAction(NoteListActions.OnDeleteNote(noteItem))
                }
            }
        )
    }
}