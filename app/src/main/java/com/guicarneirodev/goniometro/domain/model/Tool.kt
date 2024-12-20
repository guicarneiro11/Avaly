package com.guicarneirodev.goniometro.domain.model

data class Tool(
    val id: String,
    val name: String,
    val description: String,
    val isAvailable: Boolean,
    val icon: Int
)