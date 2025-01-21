package com.guicarneirodev.goniometro.presentation.ui.screens.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.presentation.ui.reusable.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.components.PreferencesSection
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.components.ToolCard
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionUiState
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.fake.FakeSelectionViewModel
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

interface SelectionViewModelInterface {
    val uiState: StateFlow<SelectionUiState>
    fun updateLanguage(language: Language)
    fun updateUserType(userType: UserType)
}

@Composable
fun SelectionScreen(
    navController: NavController, viewModel: SelectionViewModelInterface = koinViewModel<SelectionViewModel> { parametersOf(navController) }
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryDark)
    ) {
        BackgroundDecorations()

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PrimaryLight
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.tools),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryLight,
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
                    onLanguageChange = viewModel::updateLanguage,
                    onUserTypeChange = viewModel::updateUserType,
                    userProfile = uiState.userProfile,
                    navController = navController
                )
            }
        }

        uiState.errorMessage?.let { error ->
            AlertDialog(
                onDismissRequest = { /* limpar erro */ },
                title = { Text(stringResource(R.string.erro)) },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = { /* limpar erro */ }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}