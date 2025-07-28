@file:OptIn(ExperimentalTime::class)

package me.androidbox.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.androidbox.ConnectivityManager
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.core.presentation.utils.toSyncFormattedDateTime
import me.androidbox.notes.domain.NotesRepository
import me.androidbox.notes.domain.usecases.NukeAllNotesUseCase
import me.androidbox.settings.presentation.SettingsEvent.logoutSuccess
import me.androidbox.settings.presentation.SettingsEvent.onShowMessage
import me.androidbox.settings.presentation.model.SyncInterval
import me.androidbox.user.domain.User
import me.androidbox.user.domain.UserRepository
import net.orandja.either.Left
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val nukeAllNotesUseCase: NukeAllNotesUseCase,
    private val userRepository: UserRepository,
    private val noteMarkPreferences: NoteMarkPreferences,
    private val syncNoteScheduler: SyncNoteScheduler,
    private val notesRepository: NotesRepository,
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
            SharingStarted.Eagerly,
            false
        )

    init {
        viewModelScope.launch {
            val user = userRepository.fetchUser(noteMarkPreferences.getUserName()!!)

            if(user is Left) {
                _state.update { state ->
                    state.copy(
                        selectedSyncInterval = user.value.syncInterval,
                        lastSyncTime = user.value.syncTimeStamp.toSyncFormattedDateTime())
                }
            }
        }
    }

    fun action(settingsAction: SettingsAction) {
        when (settingsAction) {
            SettingsAction.OnLogout -> {
                viewModelScope.launch {
                    if (isConnected.value) {
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

                viewModelScope.launch {
                    when(settingsAction.syncInterval) {
                        SyncInterval.MINUTES_15 -> {
                            syncNoteScheduler.scheduleSync(syncTypes = SyncNoteScheduler.SyncTypes.SyncAll(interval = 15.minutes))
                        }
                        SyncInterval.MINUTES_30 -> {
                            syncNoteScheduler.scheduleSync(syncTypes = SyncNoteScheduler.SyncTypes.SyncAll(interval = 30.minutes))
                        }
                        SyncInterval.MINUTES_60 -> {
                            syncNoteScheduler.scheduleSync(syncTypes = SyncNoteScheduler.SyncTypes.SyncAll(interval = 60.minutes))
                        }
                        SyncInterval.MANUAL -> {
                            /* no-op */
                        }
                    }

                    userRepository.saveUser(
                        User(
                            userName = noteMarkPreferences.getUserName()!!,
                            syncInterval = state.value.selectedSyncInterval,
                            syncTimeStamp = Clock.System.now().toEpochMilliseconds()
                        )
                    )
                }
            }

            SettingsAction.OnSyncDataNow -> {
                viewModelScope.launch {
                    _state.update { uiState ->
                        uiState.copy(
                            isLoadingSync = true
                        )
                    }

                    notesRepository.syncPendingNotes()

                    userRepository.saveUser(
                        User(
                            userName = noteMarkPreferences.getUserName()!!,
                            syncInterval = state.value.selectedSyncInterval,
                            syncTimeStamp = Clock.System.now().toEpochMilliseconds()
                        )
                    )

                    _state.update { uiState ->
                        uiState.copy(
                            isLoadingSync = false,
                            lastSyncTime = "Just now"
                        )
                    }
                }
            }
        }
    }
}
