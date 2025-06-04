package me.androidbox.authentication.register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.register.presentation.vm.RegisterViewModel
import me.androidbox.getOrientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = koinViewModel()

    val state by viewModel.state.collectAsState()

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
}