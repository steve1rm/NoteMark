package me.androidbox.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.androidbox.core.presentation.utils.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreenRoot(
    onLogoutClicked: () -> Unit
) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = settingsViewModel.settingsEvent,
        onEvent = { settingsEvent ->
            when(settingsEvent) {
                is SettingsEvent.logoutSuccess -> {
                    if(settingsEvent.isSuccess) {
                        onLogoutClicked()
                    }
                    else {
                        /** In case we fail to logout, how to handle. Ask in Channel */
                    }
                }
            }
        }
    )

    SettingsScreen(
        state = state,
        onAction = settingsViewModel::action
    )
}