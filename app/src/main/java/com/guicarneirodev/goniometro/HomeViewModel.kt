package com.guicarneirodev.goniometro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _offsetY = MutableStateFlow(0f)
    val offsetY: StateFlow<Float> = _offsetY.asStateFlow()

    fun updateOffsetY(dragAmount: Float) {
        _offsetY.value += dragAmount
    }

    fun resetOffsetY() {
        _offsetY.value = 0f
    }
}