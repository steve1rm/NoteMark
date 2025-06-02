package me.androidbox.authentication.register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.core.presentation.utils.getOrientation
import me.androidbox.authentication.register.presentation.vm.RegisterViewModel
import me.androidbox.authentication.register.presentation.vm.RegisterViewModelFactory

@Composable
fun RegisterScreen() {
    val factory = remember { RegisterViewModelFactory() }
    val viewModel: RegisterViewModel = viewModel(factory = factory)
    if (getOrientation() == Orientation.PORTRAIT) {
        PortraitRegisterScreen(viewModel = viewModel)
    } else {
        LandscapeRegisterScreen(viewModel = viewModel)
    }
}