package me.androidbox.notes.presentation.note_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import me.androidbox.OnChangeOrientation
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import me.androidbox.notes.presentation.note_details.components.NoteDetailsActionButtons
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsEditModeLandscape
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsEditModePortrait
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsReaderModeLandscape
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsViewerModeLandscape
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsViewerModePortrait
import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode
import me.androidbox.registerBackHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditNoteScreenRoot(
    noteId: String?,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<NoteDetailsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(noteId) {
        viewModel.onAction(NoteDetailsActions.OnLoadContent(noteId))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NoteDetailsEvents.OnFailureMessage -> {
                scope.launch {
                    snackState.showSnackbar(event.message)
                }

                Logger.e {
                    event.message
                }
            }

            NoteDetailsEvents.OnSaveNoteDetailsSuccess -> {
                onNavigateBack()
            }


            NoteDetailsEvents.OnQuitScreen -> {
                onNavigateBack()
            }

            NoteDetailsEvents.OnDeleteNoteSuccess -> {
                onNavigateBack()
            }
        }
    }

    registerBackHandler {
        viewModel.onAction(NoteDetailsActions.OnBackPressed)
    }

    val screenOrientation = getOrientation()
    val isScreenPortraitMode = remember(screenOrientation) {
        screenOrientation == Orientation.PORTRAIT
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.noteId != null) {
            when (state.noteDetailsMode) {
                NoteDetailsMode.VIEWER_MODE -> {
                    OnChangeOrientation(Orientation.PORTRAIT)
                    if (isScreenPortraitMode) {
                        NoteDetailsViewerModePortrait(
                            state = state,
                            onAction = viewModel::onAction,
                            snackState = snackState
                        )
                    } else {
                        NoteDetailsViewerModeLandscape(
                            state = state,
                            onAction = viewModel::onAction,
                            snackState = snackState
                        )
                    }
                }

                NoteDetailsMode.EDIT_MODE -> {
                    if (isScreenPortraitMode) {
                        OnChangeOrientation(Orientation.PORTRAIT)
                        NoteDetailsEditModePortrait(
                            state = state,
                            onAction = viewModel::onAction,
                            snackState = snackState
                        )
                    } else {
                        NoteDetailsEditModeLandscape(
                            state = state,
                            onAction = viewModel::onAction,
                            snackState = snackState
                        )
                    }
                }

                NoteDetailsMode.READER_MODE -> {
                    OnChangeOrientation(Orientation.LANDSCAPE)
                    NoteDetailsReaderModeLandscape(
                        state = state,
                        onAction = viewModel::onAction,
                        snackState = snackState
                    )
                }
            }

            when (state.noteDetailsMode) {
                NoteDetailsMode.VIEWER_MODE, NoteDetailsMode.EDIT_MODE -> {
                    NoteDetailsActionButtons(
                        selectedAction = state.noteDetailsMode,
                        onActionClick = { noteDetailMode ->
                            viewModel.onAction(NoteDetailsActions.OnModeChange(noteDetailMode))
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                    )
                }

                NoteDetailsMode.READER_MODE -> {
                    if (state.showActionItems) {
                        NoteDetailsActionButtons(
                            selectedAction = state.noteDetailsMode,
                            onActionClick = { noteDetailMode ->
                                viewModel.onAction(NoteDetailsActions.OnModeChange(noteDetailMode))
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(8.dp)
                        )
                    }
                }
            }
        } else {
            if (isScreenPortraitMode) {
                NoteDetailsEditModePortrait(
                    state = state,
                    onAction = viewModel::onAction,
                    snackState = snackState
                )
            } else {
                NoteDetailsEditModeLandscape(
                    state = state,
                    onAction = viewModel::onAction,
                    snackState = snackState
                )
            }
        }
    }
}