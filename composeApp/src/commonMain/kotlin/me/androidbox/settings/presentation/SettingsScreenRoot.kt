package me.androidbox.settings.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.androidbox.core.presentation.utils.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreenRoot(
    onLogoutClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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

                is SettingsEvent.onShowMessage -> {
                    coroutineScope.launch {
                        snackState.showSnackbar(settingsEvent.message)
                    }
                }
            }
        }
    )

    SettingsScreen(
        state = state,
        onAction = { settingsAction ->
            when(settingsAction) {
                SettingsAction.OnBackClicked -> {
                    onBackClicked()
                }
                else -> {
                    settingsViewModel.action(settingsAction)
                }
            }
        },
        snackState = snackState
    )
}