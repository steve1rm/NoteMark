package me.androidbox.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.notes.domain.usecases.DeleteNoteUseCase
import me.androidbox.notes.domain.usecases.FetchNotesUseCase
import me.androidbox.notes.domain.usecases.GetProfilePictureUseCase
import me.androidbox.user.domain.UserRepository

class NoteListViewModel(
    private val fetchNotesUseCase: FetchNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val userRepository: UserRepository,
    private val getProfilePictureUseCase: GetProfilePictureUseCase
) : ViewModel() {
    private var hasFetched = false

    private val _state = MutableStateFlow(NoteListUiState())
    val state = _state.asStateFlow()
        .onStart {
            if (!hasFetched) {
                fetchNotes()
                getProfilePicture()
                hasFetched = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteListUiState()
        )

    private fun getProfilePicture() {
        viewModelScope.launch {
            val user = async { userRepository.fetchUser().left }
            val pictureText = async { getProfilePictureUseCase(user.await().userName) }
            _state.update { it.copy(profilePicText = pictureText.await()) }
        }
    }

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
        }
    }
}