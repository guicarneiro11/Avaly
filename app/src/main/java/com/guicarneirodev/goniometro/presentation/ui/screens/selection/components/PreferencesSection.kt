package com.guicarneirodev.goniometro.presentation.ui.screens.selection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.UserPreferences

@Composable
fun PreferencesSection(
    preferences: UserPreferences,
    onLanguageChange: (Language) -> Unit
) {
    val modifier = Modifier.padding(horizontal = 16.dp)
    var showLanguageDialog by remember { mutableStateOf<Language?>(null) }

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
                text = stringResource(R.string.configuracoes),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PreferenceItem(
                title = stringResource(R.string.idioma),
                icon = R.drawable.language
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LanguageChip(
                        text = "Português",
                        selected = preferences.language == Language.PORTUGUESE,
                        onClick = { showLanguageDialog = Language.PORTUGUESE }
                    )
                    LanguageChip(
                        text = "English",
                        selected = preferences.language == Language.ENGLISH,
                        onClick = { showLanguageDialog = Language.ENGLISH }
                    )

                    showLanguageDialog?.let { newLanguage ->
                        AlertDialog(
                            onDismissRequest = { showLanguageDialog = null },
                            title = { Text(stringResource(R.string.mudar_idioma)) },
                            text = { Text(stringResource(R.string.confirmar_mudanca_idioma)) },
                            confirmButton = {
                                TextButton(onClick = {
                                    onLanguageChange(newLanguage)
                                    showLanguageDialog = null
                                }) {
                                    Text(stringResource(R.string.confirmar))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showLanguageDialog = null }) {
                                    Text(stringResource(R.string.cancelar))
                                }
                            }
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            PreferenceItem(
                title = stringResource(R.string.app_version),
                icon = R.drawable.baseline_info_24
            ) {
                Text(
                    text = "3.0.0",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}