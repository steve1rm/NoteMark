package me.androidbox.authentication.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.login.presentation.vm.LoginViewModel
import me.androidbox.authentication.login.presentation.vm.LoginViewModelFactory
import me.androidbox.getOrientation

@Composable
fun LoginScreen(
    onNavigateToRegister : () -> Unit
) {
    val factory = remember { LoginViewModelFactory() }
    val viewModel: LoginViewModel = viewModel(factory = factory)
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