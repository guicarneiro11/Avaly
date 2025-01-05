package com.guicarneirodev.goniometro.presentation.ui.screens.selection.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int,
    onClick: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.then(
            if (onClick != null && content == null) {
                Modifier.clickable(onClick = onClick)
            } else {
                Modifier
            }
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = AccentBlue,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = SecondaryDark
            )
        }
        content?.let { content ->
            Box(modifier = Modifier.padding(start = 32.dp)) {
                content()
            }
        }
    }
}