package me.androidbox.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.SaveNoteUseCase

class EditNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase
) : ViewModel() {
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

            is EditNoteActions.OnSaveNote -> {
                viewModelScope.launch {
                    saveNoteUseCase.execute(
                        NoteItem(
                            id = "random UUID",
                            title = state.value.title,
                            content = state.value.content,
                            createdAt = Clock.System.now().toEpochMilliseconds(),
                            lastEditedAt = Clock.System.now().toEpochMilliseconds()
                        ))
                }
            }
        }
    }
}