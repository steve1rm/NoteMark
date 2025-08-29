package me.androidbox.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.ConnectivityManager
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import me.androidbox.notes.domain.usecases.FetchAllNotesUseCase
import me.androidbox.notes.domain.usecases.FetchNotesUseCase
import me.androidbox.notes.presentation.note_list.NoteListEvents.OnNavigateToEditNote

@OptIn(FlowPreview::class)
class NoteListViewModel(
    private val fetchNotesUseCase: FetchNotesUseCase,
    private val fetchAllNotesUseCase: FetchAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<NoteListEvents>()
    val events = _events.receiveAsFlow()

    private var notesObserveJob: Job? = null

    init {
        // Observe changes in the DB
        observeChangesInDb()
        fetchNotes()
        fetchConnectivityStatus()
    }

    private fun fetchNotes() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(isLoading = true)
            }

            // Fetches and inserts into DB
            fetchAllNotesUseCase.execute()

            _state.update { state ->
                state.copy(isLoading = false)
            }
        }
    }

    private fun observeChangesInDb() {
        notesObserveJob?.cancel()
        notesObserveJob = fetchNotesUseCase.execute()
            .onEach { listOfNoteItems ->
                _state.update { noteListUiState ->
                    noteListUiState.copy(
                        notesList = listOfNoteItems.toPersistentList()
                    )
                }
            }
            .launchIn(viewModelScope)   }

    private fun fetchConnectivityStatus() {
        viewModelScope.launch {
            connectivityManager
                .isConnected()
                .distinctUntilChanged()
                .collect { connected ->
                    _state.update { it.copy(isConnected = connected) }
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