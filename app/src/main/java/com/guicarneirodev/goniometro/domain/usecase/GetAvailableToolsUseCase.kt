package com.guicarneirodev.goniometro.domain.usecase

import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.domain.repository.ToolsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAvailableToolsUseCase(
    private val toolsRepository: ToolsRepository
) {
    suspend operator fun invoke(): List<Tool> = withContext(Dispatchers.IO) {
        toolsRepository.getAvailableTools()
    }
}