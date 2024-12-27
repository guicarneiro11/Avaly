package com.guicarneirodev.goniometro.presentation.ui.screens.selection.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Tool

@Composable
fun ToolCard(
    tool: Tool,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(enabled = tool.isAvailable, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (tool.isAvailable)
                Color.White.copy(alpha = 0.95f)
            else
                Color.Gray.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = tool.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = if (tool.isAvailable) Color(0xFF1E88E5) else Color.Gray
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(tool.nameResId),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (tool.isAvailable) Color.Black else Color.Gray
                )

                Text(
                    text = stringResource(tool.descriptionResId),
                    fontSize = 14.sp,
                    color = if (tool.isAvailable) Color.Gray else Color.Gray.copy(alpha = 0.7f)
                )

                if (!tool.isAvailable) {
                    Text(
                        text = stringResource(R.string.coming_soon),
                        fontSize = 12.sp,
                        color = Color(0xFF1E88E5),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}