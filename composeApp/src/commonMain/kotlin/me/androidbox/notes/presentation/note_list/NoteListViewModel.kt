package me.androidbox.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.ConnectivityManager
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import me.androidbox.notes.domain.usecases.FetchNotesUseCase
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import me.androidbox.notes.presentation.note_list.NoteListEvents.OnNavigateToEditNote

@OptIn(FlowPreview::class)
class NoteListViewModel(
    private val fetchNotesUseCase: FetchNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {
    private var hasFetched = false

    private val _state = MutableStateFlow(NoteListUiState())
    val state = _state.asStateFlow()
        .onStart {
            if (!hasFetched) {
                fetchNotes()
                hasFetched = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteListUiState()
        )

    init {
        fetchConnectivityStatus()
    }

    private fun fetchConnectivityStatus() {
        viewModelScope.launch {
            connectivityManager
                .isConnected()
                .debounce(1000)
                .distinctUntilChanged()
                .collect { connected ->
                    _state.update { it.copy(isConnected = connected) }
                }
        }
    }

    private val _events = Channel<NoteListEvents>()
    val events = _events.receiveAsFlow()

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
        when (action) {
            NoteListActions.OnCancelDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }

            is NoteListActions.OnDeleteNote -> {
                viewModelScope.launch {
                    deleteNoteUseCase.execute(action.noteItem)
                }
                _state.update {
                    it.copy(
                        showDeleteDialog = false,
                        currentSelectedNote = null
                    )
                }
            }

            is NoteListActions.OnShowDeleteDialog -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = true,
                        currentSelectedNote = action.noteItem
                    )
                }
            }

            NoteListActions.OnNavigateToEditNoteWithNewNote -> {
                viewModelScope.launch {
                    _events.send(OnNavigateToEditNote(null))
                }
            }

            is NoteListActions.OnNavigateToEditNoteWithNoteId -> {
                viewModelScope.launch {
                    _events.send(OnNavigateToEditNote(action.noteId))
                }
            }

            NoteListActions.OnSettingsClicked -> {
                /** no-op handle in navigation */
            }
        }
    }
}