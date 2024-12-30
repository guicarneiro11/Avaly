package com.guicarneirodev.goniometro.presentation.ui.screens.selection.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserProfile
import com.guicarneirodev.goniometro.domain.model.UserType

@Composable
fun PreferencesSection(
    preferences: UserPreferences,
    onLanguageChange: (Language) -> Unit,
    onUserTypeChange: (UserType) -> Unit,
    userProfile: UserProfile?,
    navController: NavController
) {
    val modifier = Modifier.padding(horizontal = 16.dp)
    var showLanguageDialog by remember { mutableStateOf<Language?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

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
            userProfile?.let {
                Text(
                    text = it.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                )

                Text(
                    text = it.email,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Text(
                text = stringResource(R.string.configuracoes),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PreferenceItem(
                title = stringResource(R.string.tipo_usuario),
                icon = R.drawable.person
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UserTypeChip(
                        text = stringResource(R.string.estudante),
                        selected = preferences.userType == UserType.STUDENT,
                        onClick = { onUserTypeChange(UserType.STUDENT) }
                    )
                    UserTypeChip(
                        text = stringResource(R.string.profissional),
                        selected = preferences.userType == UserType.PROFESSIONAL,
                        onClick = { onUserTypeChange(UserType.PROFESSIONAL) }
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

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            PreferenceItem(
                title = stringResource(R.string.logout),
                icon = R.drawable.logout
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true }
                )
            }
        }

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

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text(stringResource(R.string.confirmar_logout)) },
                text = { Text(stringResource(R.string.confirmar_logout_mensagem)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            showLogoutDialog = false
                        }
                    ) {
                        Text(stringResource(R.string.confirmar))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            )
        }
    }
}