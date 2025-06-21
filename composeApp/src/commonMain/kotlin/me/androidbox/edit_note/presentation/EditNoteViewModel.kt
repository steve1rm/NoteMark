package me.androidbox.edit_note.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditNoteViewModel : ViewModel() {
    private val _state = MutableStateFlow(EditNoteUiState())
    val state = _state.asStateFlow()

    fun onAction(action: EditNoteActions) {
        when (action) {
            is EditNoteActions.OnContentChange -> {
                _state.update { it.copy(content = action.content) }
            }
            is EditNoteActions.OnTitleChange -> {
                _state.update { it.copy(title = action.title) }
            }
        }
    }
}