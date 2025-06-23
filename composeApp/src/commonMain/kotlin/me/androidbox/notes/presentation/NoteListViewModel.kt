package me.androidbox.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.notes.domain.usecases.FetchNotesUseCase

class NoteListViewModel(
    private val fetchNotesUseCase: FetchNotesUseCase
) : ViewModel() {
    private var hasFetched = false

    private val _state = MutableStateFlow(NoteListUiState())
    val state = _state.asStateFlow()
        .onStart {
            if(!hasFetched) {
                fetchNotes()
                hasFetched = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteListUiState()
        )

    private fun fetchNotes() {
        viewModelScope.launch {
            fetchNotesUseCase.execute()
                .collect { listOfNoteItems ->
                    _state.update { noteListUiState ->
                        noteListUiState.copy(
                            notesList = listOfNoteItems
                        )
                    }
                }
        }
    }

    fun onAction(action: NoteListActions) {

    }
}