package me.androidbox.authentication.register.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.androidbox.authentication.core.AuthenticationEvents
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreenRoot(
    onNavigateToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = koinViewModel()

    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AuthenticationEvents.OnAuthenticationFail -> {
                viewModel.onAction(RegisterActions.OnSendMessage(event.message))
            }
            AuthenticationEvents.OnAuthenticationSuccess -> {
                onNavigateToLogin()
            }
        }
    }

    if (getOrientation() == Orientation.PORTRAIT) {
        PortraitRegisterScreen(
            state = state,
            onAction = viewModel::onAction,
            onNavigateToLogin = onNavigateToLogin
        )
    } else {
        LandscapeRegisterScreen(
            onAction = viewModel::onAction,
            state = state,
            onNavigateToLogin = onNavigateToLogin
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