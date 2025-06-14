package me.androidbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val showSplash: Boolean = true
)

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    init {
        manageSplash()
    }

    private fun manageSplash() {
        viewModelScope.launch {
            _state.update { it.copy(showSplash = false) }
        }
    }
}