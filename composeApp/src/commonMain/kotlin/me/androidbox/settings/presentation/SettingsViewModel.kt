package me.androidbox.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.ConnectivityManager
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.notes.domain.usecases.NukeAllNotesUseCase
import me.androidbox.settings.presentation.SettingsEvent.logoutSuccess
import me.androidbox.settings.presentation.SettingsEvent.onShowMessage

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val nukeAllNotesUseCase: NukeAllNotesUseCase,
    private val noteMarkPreferences: NoteMarkPreferences,
    private val syncNoteScheduler: SyncNoteScheduler,
    private val applicationScope: CoroutineScope,
    connectivityManager: ConnectivityManager,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenUiState())
    val state = _state.asStateFlow()

    private val _settingsEvent = Channel<SettingsEvent>()
    val settingsEvent = _settingsEvent.receiveAsFlow()

    private val isConnected = connectivityManager
        .isConnected()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun action(settingsAction: SettingsAction) {
        when (settingsAction) {
            SettingsAction.OnLogout -> {
                viewModelScope.launch {
                    isConnected.collectLatest { connected ->
                        if (connected) {
                            val refreshToken = noteMarkPreferences.getRefreshToken()

                            if (refreshToken != null) {
                                applicationScope.launch {
                                    /** We don't care about the result, as we just want to logout
                                     *  and clear the cache and navigate back to the login screen */
                                    syncNoteScheduler.cancelAllSyncs()
                                    noteMarkPreferences.deleteAllPreferences()
                                    nukeAllNotesUseCase.execute()
                                    logoutUseCase.execute(
                                        logoutRequest = LogoutRequest(
                                            refreshToken = refreshToken
                                        )
                                    )
                                    _settingsEvent.send(logoutSuccess(isSuccess = true))
                                }
                            }
                        } else {
                            _settingsEvent.trySend(
                                onShowMessage(
                                    message = "You need an internet connection to log out."
                                )
                            )
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
