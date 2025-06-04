package me.androidbox.authentication.register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.register.presentation.vm.RegisterViewModel
import me.androidbox.authentication.register.presentation.vm.RegisterViewModelFactory
import me.androidbox.getOrientation

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit
) {
    val factory = remember { RegisterViewModelFactory() }
    val viewModel: RegisterViewModel = viewModel(factory = factory)

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