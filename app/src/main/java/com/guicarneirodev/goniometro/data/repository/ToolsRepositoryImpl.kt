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
        ), Tool(
            id = "posture_analysis",
            nameResId = R.string.symmetrograph,
            descriptionResId = R.string.symmetrograph_desc,
            isAvailable = false,
            icon = R.drawable.body
        ), Tool(
            id = "range_motion",
            nameResId = R.string.tmj_tools,
            descriptionResId = R.string.tmj_tools_desc,
            isAvailable = false,
            icon = R.drawable.hearing
        ), Tool(
            id = "muscle_strength",
            nameResId = R.string.other_tools,
            descriptionResId = R.string.other_tools_desc,
            isAvailable = false,
            icon = R.drawable.next_plan
        )
    )
}