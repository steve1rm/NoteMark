package me.androidbox.authentication.login.presentation

import androidx.compose.runtime.Composable
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.core.presentation.utils.getOrientation
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen() {
    val viewModel : LoginViewModel = viewModel()
    if (getOrientation() == Orientation.PORTRAIT) {
        PortraitLoginScreen(viewModel = viewModel)
    } else {
        LandscapeLoginScreen(viewModel = viewModel)
    }
}