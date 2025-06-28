package me.androidbox.notes.presentation.edit_note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import me.androidbox.notes.presentation.components.DiscardNoteDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditNoteScreenRoot(
    noteId: String?,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<EditNoteViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(noteId) {
        viewModel.onAction(EditNoteActions.OnLoadContent(noteId))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EditNoteEvents.OnSaveNoteSuccess -> {
                onNavigateBack()
            }

            is EditNoteEvents.OnFailureMessage -> {
                Logger.e {
                    "Failed to save note"
                }
            }

            EditNoteEvents.OnDiscardNote -> {
                onNavigateBack()
            }
        }
    }

    val orientation = getOrientation()
    val isPortrait = remember(orientation) { orientation == Orientation.PORTRAIT }

    if (isPortrait) {
        EditNoteScreenPortrait(
            noteId = noteId,
            state = state,
            onAction = viewModel::onAction,
        )
    } else {
        EdiNoteScreenLandscape(
            noteId = noteId,
            state = state,
            onAction = viewModel::onAction,
        )
    }

    if (state.showDiscardDialog) {
        DiscardNoteDialog(
            onDiscardNoteClick = {
                onNavigateBack()
            },
            onKeepEditingClick = {
                viewModel.onAction(EditNoteActions.OnKeepEditingClick)
            }
        )
    }

}