package me.androidbox.notes.presentation.note_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.androidbox.generateUUID
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import me.androidbox.notes.domain.usecases.FetchNoteUseCase
import me.androidbox.notes.domain.usecases.UpdateNoteUseCase
import me.androidbox.notes.presentation.note_details.NoteDetailsEvents.OnDeleteNoteSuccess
import me.androidbox.notes.presentation.note_details.NoteDetailsEvents.OnFailureMessage
import me.androidbox.notes.presentation.note_details.NoteDetailsEvents.OnQuitScreen
import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode
import me.androidbox.notes.presentation.note_details.utils.NoteDetailsTimeFormatter
import net.orandja.either.Left
import net.orandja.either.Right

class NoteDetailsViewModel(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val fetchNoteUseCase: FetchNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NoteDetailsUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<NoteDetailsEvents>()
    val events = _events.receiveAsFlow()

    private var actionsVisibilityJob: Job? = null

    private var noteTitleSaverJob: Job? = null
    private var noteContentSaverJob: Job? = null

    fun onAction(action: NoteDetailsActions) {
        when (action) {
            is NoteDetailsActions.OnTitleChange -> {
                _state.update { it.copy(inputTitle = action.title) }

                noteTitleSaverJob?.cancel()
                noteTitleSaverJob = viewModelScope.launch {
                    delay(700)
                    updateNote()
                }
            }

            is NoteDetailsActions.OnContentChange -> {
                _state.update { it.copy(inputContent = action.content) }

                noteContentSaverJob?.cancel()

                noteContentSaverJob = viewModelScope.launch {
                    delay(700)

                    updateNote()
                }
            }

            NoteDetailsActions.OnCloseClick -> {
                viewModelScope.launch {
                    validateNoteContents()
                }
            }


            is NoteDetailsActions.OnLoadContent -> {
                viewModelScope.launch {
                    _state.update { it.copy(noteId = action.noteId) }
                    if (action.noteId == null) {
                        _state.update { state ->
                            state.copy(
                                updatingId = generateUUID(),
                                inputTitle = "Note title",
                                inputContent = "",
                                noteTitle = "Note title",
                                noteContent = "",
                            )
                        }
                    } else {
                        val noteResult = fetchNoteUseCase.fetchNote(action.noteId)
                        when (noteResult) {
                            is Left -> {
                                val noteTitle = noteResult.value.title
                                val noteContent = noteResult.value.content
                                val noteCreatedDate = noteResult.value.createdAt
                                val createdDateFormatted =
                                    NoteDetailsTimeFormatter.toFormattedDateString(
                                        timeMillis = noteResult.value.createdAt
                                    )
                                val editedDateFormatted =
                                    NoteDetailsTimeFormatter.toFormattedDateString(
                                        timeMillis = noteResult.value.lastEditedAt
                                    )
                                _state.update { state ->
                                    state.copy(
                                        updatingId = _state.value.noteId,
                                        inputTitle = noteTitle,
                                        inputContent = noteContent,
                                        noteTitle = noteTitle,
                                        noteContent = noteContent,
                                        noteEditedDateFormatted = editedDateFormatted,
                                        noteCreatedDateFormatted = createdDateFormatted,
                                        noteCreatedDateMillis = noteCreatedDate
                                    )
                                }
                            }

                            is Right -> {
                                _events.send(OnFailureMessage("Failed to load note"))
                            }
                        }
                    }
                }
            }

            is NoteDetailsActions.OnModeChange -> {
                if (action.mode == _state.value.noteDetailsMode
                    && _state.value.noteDetailsMode != NoteDetailsMode.VIEWER_MODE
                ) {
                    _state.update { it.copy(noteDetailsMode = NoteDetailsMode.VIEWER_MODE) }
                } else {
                    when (action.mode) {
                        NoteDetailsMode.VIEWER_MODE, NoteDetailsMode.EDIT_MODE -> {
                            _state.update {
                                it.copy(
                                    noteDetailsMode = action.mode,
                                    showActionItems = true
                                )
                            }
                        }

                        NoteDetailsMode.READER_MODE -> {
                            actionsVisibilityJob?.cancel()
                            _state.update {
                                it.copy(
                                    noteDetailsMode = action.mode,
                                    showActionItems = true
                                )
                            }

                            actionsVisibilityJob = viewModelScope.launch {
                                delay(5000)
                                _state.update {
                                    it.copy(showActionItems = false)
                                }
                                actionsVisibilityJob = null
                            }
                        }
                    }
                }
            }

            NoteDetailsActions.OnReaderScreenClick -> {
                if (actionsVisibilityJob?.isActive == true) {
                    actionsVisibilityJob?.cancel()
                    actionsVisibilityJob = null
                    _state.update { it.copy(showActionItems = true) }
                } else {
                    actionsVisibilityJob = viewModelScope.launch {
                        _state.update { it.copy(showActionItems = true) }
                        delay(5000)
                        _state.update { it.copy(showActionItems = false) }
                        actionsVisibilityJob = null
                    }
                }
            }

            NoteDetailsActions.OnBackPressed -> {
                validateNoteContents()
            }
        }
    }

    private fun validateNoteContents() {
        viewModelScope.launch {
            if (_state.value.inputContent.isBlank() && _state.value.inputTitle.isBlank()) {
                val noteItem = NoteItem(
                    id = _state.value.noteId!!,
                    title = _state.value.inputTitle,
                    content = _state.value.inputContent,
                    createdAt = _state.value.noteCreatedDateMillis ?: Clock.System.now()
                        .toEpochMilliseconds(),
                    lastEditedAt = Clock.System.now().toEpochMilliseconds()
                )
                val result = deleteNoteUseCase.execute(noteItem)
                when (result) {
                    is Left -> {
                        _events.send(OnDeleteNoteSuccess)
                    }

                    is Right -> {
                        _events.send(OnFailureMessage("Failed to delete note"))
                    }
                }
            } else {
                _events.send(OnQuitScreen)
            }
        }
    }

    private suspend fun updateNote() {
        val currentTimestamp = Clock.System.now().toEpochMilliseconds()
        val formattedTime = NoteDetailsTimeFormatter.toFormattedDateString(currentTimestamp)
        val state = _state.value

        val noteItem = NoteItem(
            id = state.updatingId!!,
            title = state.inputTitle,
            content = state.inputContent,
            createdAt = state.noteCreatedDateMillis ?: currentTimestamp,
            lastEditedAt = currentTimestamp
        )

        val result = updateNoteUseCase.execute(noteItem)

        when (result) {
            is Left -> {
                _state.update {
                    it.copy(
                        noteTitle = state.inputTitle,
                        noteContent = state.inputContent,
                        noteEditedDateFormatted = formattedTime
                    )
                }
            }

            is Right -> {
                viewModelScope.launch {
                    _events.send(OnFailureMessage("Failed to update note"))
                }
            }
        }
    }

}