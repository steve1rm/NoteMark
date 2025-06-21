package me.androidbox.notes.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.androidbox.core.models.Orientation
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditNoteScreenRoot() {
    val viewModel = koinViewModel<EditNoteViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val orientation = getOrientation()
    val isPortrait = orientation == Orientation.PORTRAIT

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