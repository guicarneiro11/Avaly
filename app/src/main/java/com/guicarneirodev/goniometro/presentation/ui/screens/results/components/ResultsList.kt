package com.guicarneirodev.goniometro.presentation.ui.screens.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.domain.repository.AngleData

@Composable
fun ResultsList(
    angles: List<AngleData>,
    searchQuery: String,
    onEditAngle: (AngleData) -> Unit,
    onDeleteAngle: (String) -> Unit
) {
    val filteredAngles = remember(angles, searchQuery) {
        if (searchQuery.isEmpty()) {
            angles
        } else {
            angles.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.value.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(filteredAngles, key = { it.id }) { angle ->
            AngleCard(
                angle = angle,
                onEdit = { onEditAngle(angle) },
                onDelete = {
                    onDeleteAngle(angle.id)
                }
            )
        }
    }
}