package com.guicarneirodev.goniometro.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.guicarneirodev.goniometro.domain.model.GoniometryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoniometroScreenViewModel : ViewModel() {
    private val _lineStart = mutableStateOf(Offset.Zero)
    val lineStart: State<Offset> = _lineStart

    private val _lineEnd = mutableStateOf(Offset.Zero)
    val lineEnd: State<Offset> = _lineEnd

    private val _lines = mutableStateOf<List<Pair<Offset, Offset>>>(emptyList())
    val lines: State<List<Pair<Offset, Offset>>> = _lines

    private val _isLineSet = mutableStateOf(false)
    val isLineSet: State<Boolean> = _isLineSet

    private val _selectedAngleIndex = mutableIntStateOf(0)
    val selectedAngleIndex: State<Int> = _selectedAngleIndex

    private val _currentImageUri = mutableStateOf<Uri?>(null)
    val currentImageUri: State<Uri?> = _currentImageUri

    private val _uiState = MutableStateFlow<GoniometryState>(GoniometryState.Initial)
    val uiState = _uiState.asStateFlow()

    fun setLineStart(offset: Offset) {
        _lineStart.value = offset
    }

    fun setLineEnd(offset: Offset) {
        _lineEnd.value = offset
    }

    fun addLine(line: Pair<Offset, Offset>) {
        _lines.value += line
    }

    fun clearLines() {
        _lines.value = emptyList()
        _lineStart.value = Offset.Zero
        _lineEnd.value = Offset.Zero
    }

    fun toggleLineSet() {
        _isLineSet.value = !_isLineSet.value
    }

    fun setSelectedAngleIndex(index: Int) {
        _selectedAngleIndex.intValue = index
    }

    fun setCurrentImageUri(uri: Uri?) {
        _uiState.value = GoniometryState.Ready(
            imageUri = uri,
            isLineSet = false,
            lines = emptyList(),
            currentQuadrant = selectedAngleIndex.value
        )
    }
}