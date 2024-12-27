package com.guicarneirodev.goniometro.domain.model

data class Tool(
    val id: String,
    val nameResId: Int,
    val descriptionResId: Int,
    val isAvailable: Boolean,
    val icon: Int
)