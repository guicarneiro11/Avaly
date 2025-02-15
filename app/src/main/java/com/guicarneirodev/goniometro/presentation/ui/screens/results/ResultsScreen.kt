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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.repository.AngleData
import com.guicarneirodev.goniometro.presentation.ui.reusable.SearchField
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.components.DeleteConfirmationDialog
import com.guicarneirodev.goniometro.presentation.ui.screens.results.components.EditDialog
import com.guicarneirodev.goniometro.presentation.ui.screens.results.components.ResultsAppBar
import com.guicarneirodev.goniometro.presentation.ui.screens.results.components.ResultsList
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.UiState
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
    var showDeleteAngleConfirmation by remember { mutableStateOf(false) }
    var angleToDelete by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

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
                    onDeleteAngle = { angleId ->
                        angleToDelete = angleId
                        showDeleteAngleConfirmation = true
                    }
                )
            }
        }

        LaunchedEffect(uiState) {
            when (uiState) {
                is UiState.Success -> {
                    snackbarHostState.showSnackbar(
                        message = (uiState as UiState.Success).message,
                        duration = SnackbarDuration.Short
                    )
                    // Reset state after showing snackbar
                    viewModel.resetUiState()
                }
                is UiState.Error -> {
                    snackbarHostState.showSnackbar(
                        message = (uiState as UiState.Error).message,
                        duration = SnackbarDuration.Long
                    )
                }
                else -> {}
            }
        }
    }

    if (showEditDialog) {
        currentlyEditing?.let { angle ->
            EditDialog(
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
    if (showDeleteAngleConfirmation) {
        DeleteConfirmationDialog(
            title = stringResource(R.string.confirm_delete_angle_title),
            message = stringResource(R.string.confirm_delete_angle_message),
            onDismiss = {
                showDeleteAngleConfirmation = false
                angleToDelete = null
            },
            onConfirm = {
                angleToDelete?.let { viewModel.deleteAngle(it) }
                showDeleteAngleConfirmation = false
                angleToDelete = null
            }
        )
    }
}