package com.guicarneirodev.goniometro.data.repository

import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.domain.repository.ToolsRepository

class ToolsRepositoryImpl : ToolsRepository {
    override suspend fun getAvailableTools(): List<Tool> = listOf(
        Tool(
            id = "goniometer",
            name = "Goniômetro",
            description = "Meça ângulos articulares com precisão",
            isAvailable = true,
            icon = R.drawable.goniometro
        ),
        Tool(
            id = "posture_analysis",
            name = "Análise Postural",
            description = "Avalie a postura do paciente",
            isAvailable = false,
            icon = R.drawable.next_plan
        ),
        Tool(
            id = "range_motion",
            name = "Amplitude de Movimento",
            description = "Avalie ADM com referências visuais",
            isAvailable = false,
            icon = R.drawable.next_plan
        ),
        Tool(
            id = "muscle_strength",
            name = "Força Muscular",
            description = "Registre avaliações de força",
            isAvailable = false,
            icon = R.drawable.next_plan
        )
    )
}
