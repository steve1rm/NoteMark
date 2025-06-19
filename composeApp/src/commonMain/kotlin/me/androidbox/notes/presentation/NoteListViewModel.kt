package me.androidbox.notes.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteListViewModel : ViewModel() {
    private val _state = MutableStateFlow(NoteListUiState())
    val state = _state.asStateFlow()

    fun onAction(action: NoteListActions) {

    }
}