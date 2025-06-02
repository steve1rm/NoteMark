package me.androidbox.authentication.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.core.presentation.utils.getOrientation
import me.androidbox.authentication.login.presentation.vm.LoginViewModel
import me.androidbox.authentication.login.presentation.vm.LoginViewModelFactory

@Composable
fun LoginScreen() {
    val factory = remember { LoginViewModelFactory() }
    val viewModel: LoginViewModel = viewModel(factory = factory)
    if (getOrientation() == Orientation.PORTRAIT) {
        PortraitLoginScreen(viewModel = viewModel)
    } else {
        LandscapeLoginScreen(viewModel = viewModel)
    }
}