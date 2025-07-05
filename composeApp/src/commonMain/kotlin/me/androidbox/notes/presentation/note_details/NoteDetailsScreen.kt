package me.androidbox.notes.presentation.note_details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import me.androidbox.OnChangeOrientation
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import me.androidbox.notes.presentation.components.DiscardNoteDialog
import me.androidbox.notes.presentation.note_details.components.NoteDetailsActionButtons
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsEditModeLandscape
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsEditModePortrait
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsReaderModeLandscape
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsViewerModeLandscape
import me.androidbox.notes.presentation.note_details.mode_screens.NoteDetailsViewerModePortrait
import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditNoteScreenRoot(
    noteId: String?,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<NoteDetailsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(noteId) {
        viewModel.onAction(NoteDetailsActions.OnLoadContent(noteId))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NoteDetailsEvents.OnSaveNoteDetailsSuccess -> {
                onNavigateBack()
            }

            is NoteDetailsEvents.OnFailureMessage -> {
                Logger.e {
                    "Failed to save note"
                }
            }

            NoteDetailsEvents.OnDiscardNoteDetails -> {
                onNavigateBack()
            }
        }
    }

    val screenOrientation = getOrientation()
    val isScreenPortraitMode =
        remember(screenOrientation) { screenOrientation == Orientation.PORTRAIT }

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
                        )
                    } else {
                        NoteDetailsViewerModeLandscape(
                            state = state,
                            onAction = viewModel::onAction,
                        )
                    }
                }

                NoteDetailsMode.EDIT_MODE -> {
                    if (isScreenPortraitMode) {
                        OnChangeOrientation(Orientation.PORTRAIT)
                        NoteDetailsEditModePortrait(
                            noteId = noteId,
                            state = state,
                            onAction = viewModel::onAction,
                        )
                    } else {
                        NoteDetailsEditModeLandscape(
                            noteId = noteId,
                            state = state,
                            onAction = viewModel::onAction,
                        )
                    }
                }

                NoteDetailsMode.READER_MODE -> {
                    OnChangeOrientation(Orientation.LANDSCAPE)
                    NoteDetailsReaderModeLandscape(
                        state = state,
                        onAction = viewModel::onAction,
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
                    noteId = noteId,
                    state = state,
                    onAction = viewModel::onAction,
                )
            } else {
                NoteDetailsEditModeLandscape(
                    noteId = noteId,
                    state = state,
                    onAction = viewModel::onAction,
                )
            }
        }
    }

    if (state.showDiscardDialog) {
        DiscardNoteDialog(
            onDiscardNoteClick = onNavigateBack,
            onKeepEditingClick = {
                viewModel.onAction(NoteDetailsActions.OnKeepEditingClick)
            }
        )
    }
}