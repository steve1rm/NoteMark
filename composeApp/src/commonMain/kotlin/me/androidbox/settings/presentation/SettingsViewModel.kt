package me.androidbox.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import net.orandja.either.Left
import net.orandja.either.Right

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val noteMarkPreferences: NoteMarkPreferences
) : ViewModel() {

    private val _settingsEvent = Channel<SettingsEvent>()
    val settingsEvent = _settingsEvent.receiveAsFlow()

    fun action(settingsAction: SettingsAction) {
        when(settingsAction) {
            SettingsAction.onLogout -> {
                viewModelScope.launch {
                    val refreshToken = noteMarkPreferences.getRefreshToken()

                    if(refreshToken != null) {
                        val result = logoutUseCase.execute(
                            logoutRequest = LogoutRequest(
                                refreshToken = refreshToken
                            )
                        )

                        when(result) {
                            is Left -> {
                                _settingsEvent.send(SettingsEvent.logoutSuccess(isSuccess = true))
                            }
                            is Right -> {
                                _settingsEvent.send(SettingsEvent.logoutSuccess(isSuccess = false))
                            }
                        }
                    }
                }
            }
        }
    }
}
