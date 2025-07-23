package me.androidbox.notes.presentation.note_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
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
import me.androidbox.notes.domain.usecases.FetchNoteUseCase
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import me.androidbox.notes.domain.usecases.UpdateNoteUseCase
import me.androidbox.notes.presentation.note_details.NoteDetailsEvents.OnDiscardNoteDetails
import me.androidbox.notes.presentation.note_details.NoteDetailsEvents.OnFailureMessage
import me.androidbox.notes.presentation.note_details.NoteDetailsEvents.OnSaveNoteDetailsSuccess
import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode
import me.androidbox.notes.presentation.note_details.utils.NoteDetailsTimeFormatter
import net.orandja.either.Left
import net.orandja.either.Right

class NoteDetailsViewModel(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val fetchNoteUseCase: FetchNoteUseCase
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
                    val noteItem = NoteItem(
                        id = _state.value.noteId ?: generateUUID(),
                        title = action.title,
                        content = _state.value.inputContent,
                        createdAt = _state.value.noteCreatedDateMillis ?: Clock.System.now()
                            .toEpochMilliseconds(),
                        lastEditedAt = Clock.System.now().toEpochMilliseconds()
                    )
                    updateNoteUseCase.execute(noteItem)
                }
            }

            is NoteDetailsActions.OnContentChange -> {
                _state.update { it.copy(inputContent = action.content) }

                noteContentSaverJob?.cancel()
                noteContentSaverJob = viewModelScope.launch {
                    delay(700)
                    val noteItem = NoteItem(
                        id = _state.value.noteId ?: generateUUID(),
                        title = _state.value.inputTitle,
                        content = action.content,
                        createdAt = _state.value.noteCreatedDateMillis ?: Clock.System.now()
                            .toEpochMilliseconds(),
                        lastEditedAt = Clock.System.now().toEpochMilliseconds()
                    )
                    updateNoteUseCase.execute(noteItem)
                }
            }

            is NoteDetailsActions.OnSaveNote -> {
                viewModelScope.launch {
                    Logger.d {
                        "Saving the note ${state.value.inputTitle}"
                    }

                    val noteItem = NoteItem(
                        id = _state.value.noteId ?: generateUUID(),
                        title = _state.value.inputTitle,
                        content = _state.value.inputContent,
                        createdAt = _state.value.noteCreatedDateMillis ?: Clock.System.now()
                            .toEpochMilliseconds(),
                        lastEditedAt = Clock.System.now().toEpochMilliseconds()
                    )

                    val result = when (state.value.noteDetailsMode) {
                        NoteDetailsMode.EDIT_MODE -> {
                            updateNoteUseCase.execute(noteItem = noteItem)
                        }

                        NoteDetailsMode.VIEWER_MODE -> {
                            saveNoteUseCase.execute(noteItem = noteItem)
                        }

                        NoteDetailsMode.READER_MODE -> {
                            /** no-op in viewer mode */
                            null
                        }
                    }

                    if (result != null) {
                        when (result) {
                            is Left -> {
                                Logger.d {
                                    "Saved to the local database and updated the remote"
                                }
                                _events.send(OnSaveNoteDetailsSuccess)
                            }

                            is Right -> {
                                Logger.e {
                                    "Failed to upload the note ${result.right}"
                                }
                                _events.send(OnFailureMessage(result.right.toString()))
                            }
                        }
                    }
                }
            }

            NoteDetailsActions.OnCloseClick -> {
                if (
                    _state.value.inputTitle != _state.value.noteTitle ||
                    _state.value.inputContent != _state.value.noteContent
                ) {
                    _state.update { it.copy(showDiscardDialog = true) }
                } else {
                    viewModelScope.launch {
                        _events.send(OnDiscardNoteDetails)
                    }
                }
            }

            NoteDetailsActions.OnKeepEditingClick -> {
                _state.update { it.copy(showDiscardDialog = false) }
            }

            is NoteDetailsActions.OnLoadContent -> {
                viewModelScope.launch {
                    _state.update { it.copy(noteId = action.noteId) }
                    if (action.noteId == null) {
                        _state.update { state ->
                            state.copy(
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
        }
    }
}