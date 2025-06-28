package me.androidbox.notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.androidbox.generateUUID
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import me.androidbox.notes.domain.usecases.FetchNotesUseCase
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import me.androidbox.notes.presentation.note_list.NoteListEvents.*
import net.orandja.either.Left
import net.orandja.either.Right

class NoteListViewModel(
    private val fetchNotesUseCase: FetchNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
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
//                    val noteId = generateUUID()
//                    val noteItem = NoteItem(
//                        id = noteId,
//                        title = "Note title",
//                        content = "",
//                        createdAt = Clock.System.now().toEpochMilliseconds(),
//                        lastEditedAt = Clock.System.now().toEpochMilliseconds()
//                    )
//                    val result = saveNoteUseCase.execute(noteItem)
                    _events.send(OnNavigateToEditNote(null))

//                    when (result) {
//                        is Left -> {
//                        }
//
//                        is Right -> {
//                            _events.send(OnFailureMessage("Failed to create new note"))
//                        }
//                    }
                }
            }

            is NoteListActions.OnNavigateToEditNoteWithNoteId -> {
                viewModelScope.launch {
                    _events.send(NoteListEvents.OnNavigateToEditNote(action.noteId))
                }
            }
        }
    }
}