package me.androidbox.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.notes.domain.usecases.NukeAllNotesUseCase
import me.androidbox.settings.presentation.SettingsEvent.*
import net.orandja.either.Left
import net.orandja.either.Right

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val nukeAllNotesUseCase: NukeAllNotesUseCase,
    private val noteMarkPreferences: NoteMarkPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenUiState())
    val state = _state.asStateFlow()

    private val _settingsEvent = Channel<SettingsEvent>()
    val settingsEvent = _settingsEvent.receiveAsFlow()

    fun action(settingsAction: SettingsAction) {
        when (settingsAction) {
            SettingsAction.OnLogout -> {
                viewModelScope.launch {
                    val refreshToken = noteMarkPreferences.getRefreshToken()

                    if (refreshToken != null) {
                        val result = logoutUseCase.execute(
                            logoutRequest = LogoutRequest(
                                refreshToken = refreshToken
                            )
                        )

                        when (result) {
                            is Left -> {
                                noteMarkPreferences.deleteAllPreferences()
                                nukeAllNotesUseCase.execute()
                                _settingsEvent.send(logoutSuccess(isSuccess = true))
                            }

                            is Right -> {
                                _settingsEvent.send(logoutSuccess(isSuccess = false))
                            }
                        }
                    }
                }
            }

            SettingsAction.OnSyncIntervalOptionClicked -> {
                _state.update { uiState ->
                    uiState.copy(
                        isSyncIntervalPopupVisible = !uiState.isSyncIntervalPopupVisible
                    )
                }
            }

            is SettingsAction.OnSyncIntervalSelected -> {
                _state.update { uiState ->
                    uiState.copy(
                        selectedSyncInterval = settingsAction.syncInterval,
                        isSyncIntervalPopupVisible = false
                    )
                }
            }
        }
    }
}
