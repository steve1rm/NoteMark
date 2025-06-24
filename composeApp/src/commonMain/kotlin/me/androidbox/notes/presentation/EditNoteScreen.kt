package me.androidbox.notes.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditNoteScreenRoot(
    onNavigateBack : () -> Unit,
) {
    val viewModel = koinViewModel<EditNoteViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EditNoteEvents.OnSaveNoteSuccess -> {
                onNavigateBack()
            }
            is EditNoteEvents.OnSaveNoteFail -> {
                Logger.e {
                    "Failed to save note"
                }
            }
        }
    }

    val orientation = getOrientation()
    val isPortrait = remember(orientation) { orientation == Orientation.PORTRAIT }

    if (isPortrait) {
        EditNoteScreenPortrait(
            state = state,
            onAction = viewModel::onAction
        )
    } else {
        EdiNoteScreenLandscape(
            state = state,
            onAction = viewModel::onAction
        )

    }

}