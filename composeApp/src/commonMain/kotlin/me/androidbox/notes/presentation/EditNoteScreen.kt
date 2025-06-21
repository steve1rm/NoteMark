package me.androidbox.notes.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import me.androidbox.core.models.Orientation
import me.androidbox.getOrientation

@Composable
fun EditNoteScreenRoot() {
    val viewModel = viewModel<EditNoteViewModel>()
    val state by viewModel.state.collectAsState()

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