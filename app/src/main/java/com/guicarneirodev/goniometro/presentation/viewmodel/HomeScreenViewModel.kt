package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val offsetY: Float = 0f,
    val isLoading: Boolean = false
)

class HomeScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateOffsetY(dragAmount: Float) {
        _uiState.update { it.copy(offsetY = it.offsetY + dragAmount) }
    }

    fun resetOffsetY() {
        _uiState.update { it.copy(offsetY = 0f) }
    }
}