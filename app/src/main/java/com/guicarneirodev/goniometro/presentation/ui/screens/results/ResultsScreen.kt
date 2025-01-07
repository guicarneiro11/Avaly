package com.guicarneirodev.goniometro.presentation.ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.domain.repository.AngleData
import com.guicarneirodev.goniometro.presentation.ui.reusable.SearchField
import com.guicarneirodev.goniometro.presentation.ui.screens.results.components.ModernEditDialog
import com.guicarneirodev.goniometro.presentation.ui.screens.results.components.ResultsAppBar
import com.guicarneirodev.goniometro.presentation.ui.screens.results.components.ResultsList
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResultsScreen(navController: NavController, userId: String, patientId: String) {
    val viewModel: ResultsScreenViewModel = koinViewModel { parametersOf(userId, patientId) }
    val angles by viewModel.angles.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var currentlyEditing by remember { mutableStateOf<AngleData?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ResultsAppBar(
                navController = navController,
                onAddAngle = viewModel::addAngle
            )
        },
        containerColor = SecondaryDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SecondaryDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                SearchField(
                    value = searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ResultsList(
                    angles = angles,
                    searchQuery = searchQuery,
                    onEditAngle = { angle ->
                        currentlyEditing = angle
                        showEditDialog = true
                    },
                    onDeleteAngle = viewModel::deleteAngle
                )
            }
        }
    }

    if (showEditDialog) {
        currentlyEditing?.let { angle ->
            ModernEditDialog(
                angle = angle,
                onDismiss = {
                    showEditDialog = false
                    currentlyEditing = null
                },
                onConfirm = { newName, newValue ->
                    viewModel.updateAngle(angle.id, newName, newValue)
                    showEditDialog = false
                    currentlyEditing = null
                }
            )
        }
    }
}