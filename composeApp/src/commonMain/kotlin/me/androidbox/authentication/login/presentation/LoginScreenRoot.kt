package me.androidbox.authentication.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import me.androidbox.core.models.Orientation
import me.androidbox.core.presentation.utils.ObserveAsEvents
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    onNavigateToRegister: () -> Unit,
    onNavigateToBlankScreen : () -> Unit
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvents.OnLoginFail -> {
                viewModel.onAction(LoginActions.OnSendMessage("Invalid login credentials"))
            }

            LoginEvents.OnLoginSuccess -> {
                viewModel.onAction(LoginActions.OnSendMessage("You successfully logged in"))
                onNavigateToBlankScreen()
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
}