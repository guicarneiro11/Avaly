package com.guicarneirodev.goniometro.presentation.ui.screens.selection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.UserPreferences

@Composable
fun PreferencesSection(
    preferences: UserPreferences,
    onLanguageChange: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Configurações",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PreferenceItem(
                title = "Idioma",
                icon = R.drawable.language
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LanguageChip(
                        text = "Português",
                        selected = preferences.language == Language.PORTUGUESE,
                        onClick = { onLanguageChange(Language.PORTUGUESE) }
                    )
                    LanguageChip(
                        text = "English",
                        selected = preferences.language == Language.ENGLISH,
                        onClick = { onLanguageChange(Language.ENGLISH) }
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            PreferenceItem(
                title = "Versão do Aplicativo",
                icon = R.drawable.baseline_info_24
            ) {
                Text(
                    text = "1.0.0",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}