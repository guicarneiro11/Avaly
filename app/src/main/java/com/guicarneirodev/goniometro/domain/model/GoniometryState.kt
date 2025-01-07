package com.guicarneirodev.goniometro.domain.model

import android.net.Uri
import androidx.compose.ui.geometry.Offset

sealed class GoniometryState {
    data object Initial : GoniometryState()
    data class Ready(
        val imageUri: Uri?,
        val isLineSet: Boolean,
        val lines: List<Pair<Offset, Offset>>,
        val currentQuadrant: Int,
        val measurementValue: Double? = null
    ) : GoniometryState()
}