package me.androidbox.notes.presentation.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.FetchNoteUseCase
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import me.androidbox.notes.presentation.edit_note.EditNoteEvents.*
import net.orandja.either.Left
import net.orandja.either.Right

class EditNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val fetchNoteUseCase: FetchNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EditNoteUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditNoteEvents>()
    val events = _events.receiveAsFlow()

    fun onAction(action: EditNoteActions) {
        when (action) {
            is EditNoteActions.OnContentChange -> {
                _state.update { it.copy(inputContent = action.content) }
            }

            is EditNoteActions.OnTitleChange -> {
                _state.update { it.copy(inputTitle = action.title) }
            }

            is EditNoteActions.OnSaveNote -> {
                viewModelScope.launch {
                    Logger.d {
                        "Saving the note ${state.value.inputTitle}"
                    }


                    // FIXME
                    val result = saveNoteUseCase.execute(
                        NoteItem(
                            id = action.noteId,
                            title = state.value.inputTitle,
                            content = state.value.inputContent,
                            createdAt = Clock.System.now().toEpochMilliseconds(),
                            lastEditedAt = Clock.System.now().toEpochMilliseconds()
                        )
                    )

                    when (result) {
                        is Left -> {
                            Logger.d {
                                "Saved to the local database and updated the remote"
                            }
                            _events.send(OnSaveNoteSuccess)
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

            EditNoteActions.OnCloseClick -> {
                if (
                    _state.value.inputTitle != _state.value.noteTitle ||
                    _state.value.inputContent != _state.value.noteContent
                ) {
                    _state.update { it.copy(showDiscardDialog = true) }
                } else {
                    viewModelScope.launch {
                        _events.send(OnDiscardNote)
                    }
                }
            }

            EditNoteActions.OnKeepEditingClick -> {
                _state.update { it.copy(showDiscardDialog = false) }
            }

            is EditNoteActions.OnLoadContent -> {
                viewModelScope.launch {
                    val noteResult = fetchNoteUseCase.fetchNote(action.noteId)
                    when (noteResult) {
                        is Left -> {
                            val noteTitle = noteResult.value.title
                            val noteContent = noteResult.value.content
                            _state.update { state ->
                                state.copy(
                                    inputTitle = noteTitle,
                                    inputContent = noteContent,
                                    noteTitle = noteTitle,
                                    noteContent = noteContent,
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
    }
}