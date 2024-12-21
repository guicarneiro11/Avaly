package com.guicarneirodev.goniometro.presentation.ui.screens.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.components.PreferencesSection
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.components.ToolCard
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.components.UserProfileSection
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SelectionScreen(
    navController: NavController
) {
    val viewModel : SelectionViewModel = koinViewModel { parametersOf(navController) }
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5),
                        Color(0xFF4FC3F7)
                    )
                )
            )
    ) {
        BackgroundDecorations()

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                uiState.userProfile?.let { profile ->
                    UserProfileSection(
                        userProfile = profile,
                        userType = uiState.userPreferences.userType,
                        onUserTypeChange = viewModel::updateUserType
                    )
                }
            }

            item {
                Text(
                    text = "Ferramentas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(uiState.tools) { tool ->
                ToolCard(
                    tool = tool,
                    onClick = {
                        if (tool.id == "goniometer") {
                            navController.navigate("main")
                        }
                    }
                )
            }

            item {
                PreferencesSection(
                    preferences = uiState.userPreferences,
                    onLanguageChange = viewModel::updateLanguage
                )
            }
        }
    }
}