package me.androidbox

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    initializeKoin()

    App()
}