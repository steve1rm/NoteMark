package me.androidbox.authentication.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.login.presentation.LoginViewModel
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    onNavigateToRegister : () -> Unit
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

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