package me.androidbox.notes.presentation

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
import me.androidbox.generateUUID
import me.androidbox.notes.domain.model.NoteItem
import me.androidbox.notes.domain.usecases.SaveNoteUseCase
import net.orandja.either.Left
import net.orandja.either.Right

class EditNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EditNoteUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditNoteEvents>()
    val events = _events.receiveAsFlow()

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
                    Logger.d {
                        "Saving the note ${state.value.title}"
                    }

                    val result = saveNoteUseCase.execute(
                        NoteItem(
                            id = generateUUID(),
                            title = state.value.title,
                            content = state.value.content,
                            createdAt = Clock.System.now().toEpochMilliseconds(),
                            lastEditedAt = Clock.System.now().toEpochMilliseconds()
                        )
                    )

                    when (result) {
                        is Left -> {
                            Logger.d {
                                "Saved to the local database and updated the remote"
                            }
                            _events.send(EditNoteEvents.OnSaveNoteSuccess)
                        }

                        is Right -> {
                            Logger.e {
                                "Failed to upload the note ${result.right}"
                            }
                            _events.send(EditNoteEvents.OnSaveNoteFail(result.right.toString()))
                        }
                    }
                }
            }
        }
    }
}