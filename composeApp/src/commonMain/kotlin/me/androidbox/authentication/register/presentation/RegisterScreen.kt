package me.androidbox.authentication.register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import me.androidbox.authentication.core.presentation.models.Orientation
import me.androidbox.authentication.core.presentation.utils.getOrientation
import me.androidbox.authentication.register.presentation.vm.RegisterViewModel
import me.androidbox.authentication.register.presentation.vm.RegisterViewModelFactory

@Composable
fun RegisterScreen() {
    val factory = remember { RegisterViewModelFactory() }
    val viewModel: RegisterViewModel = viewModel(factory = factory)

    val state = viewModel.state.value

    if (getOrientation() == Orientation.PORTRAIT) {
        PortraitRegisterScreen(
            state = state,
            onAction = viewModel::onAction
        )
    } else {
        LandscapeRegisterScreen(viewModel = viewModel)
    }
}