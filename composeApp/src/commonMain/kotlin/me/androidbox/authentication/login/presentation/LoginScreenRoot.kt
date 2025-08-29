package me.androidbox.authentication.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.androidbox.authentication.core.AuthenticationEvents
import me.androidbox.core.presentation.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    onNavigateToRegister: () -> Unit,
    onNavigateToNoteListScreen : (username: String) -> Unit
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AuthenticationEvents.OnAuthenticationFail -> {
                viewModel.onAction(LoginActions.OnSendMessage(event.message))
            }

            is AuthenticationEvents.OnAuthenticationSuccess -> {
                onNavigateToNoteListScreen(event.username)
            }
        }
    }

    if (getOrientation() == Orientation.PORTRAIT) {
        PortraitLoginScreen(
            onAction = viewModel::onAction,
            state = state,
            onNavigateToRegister = onNavigateToRegister
        )
    } else {
        LandscapeLoginScreen(
            onAction = viewModel::onAction,
            state = state,
            onNavigateToRegister = onNavigateToRegister
        )
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}