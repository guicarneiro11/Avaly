package com.guicarneirodev.goniometro.domain.repository

import com.guicarneirodev.goniometro.domain.model.Tool

interface ToolsRepository {
    suspend fun getAvailableTools(): List<Tool>
}