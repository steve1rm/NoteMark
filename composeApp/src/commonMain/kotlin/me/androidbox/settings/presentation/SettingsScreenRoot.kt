package me.androidbox.settings.presentation

import androidx.compose.runtime.Composable
import me.androidbox.core.presentation.utils.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreenRoot() {
    val settingsViewModel = koinViewModel<SettingsViewModel>()

    ObserveAsEvents(
        flow = settingsViewModel.settingsEvent,
        onEvent = { settingsEvent ->
            when(settingsEvent) {
                is SettingsEvent.logoutSuccess -> {
                    if(settingsEvent.isSuccess) {

                    }
                    else {

                    }
                }
            }
        }
    )

    SettingsScreen(
        onAction = settingsViewModel::action
    )
}