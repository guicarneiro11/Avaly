package com.guicarneirodev.goniometro.data.repository

import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.domain.repository.ToolsRepository

class ToolsRepositoryImpl : ToolsRepository {
    override suspend fun getAvailableTools(): List<Tool> = listOf(
        Tool(
            id = "goniometer",
            nameResId = R.string.goniometer,
            descriptionResId = R.string.goniometer_desc,
            isAvailable = true,
            icon = R.drawable.goniometro
        ),
        Tool(
            id = "posture_analysis",
            nameResId = R.string.posture_analysis,
            descriptionResId = R.string.posture_desc,
            isAvailable = false,
            icon = R.drawable.next_plan
        ),
        Tool(
            id = "range_motion",
            nameResId = R.string.range_motion,
            descriptionResId = R.string.range_motion_desc,
            isAvailable = false,
            icon = R.drawable.next_plan
        ),
        Tool(
            id = "muscle_strength",
            nameResId = R.string.muscle_strength,
            descriptionResId = R.string.muscle_desc,
            isAvailable = false,
            icon = R.drawable.next_plan
        )
    )
}