package com.guicarneirodev.goniometro.presentation.ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
import com.guicarneirodev.goniometro.data.repository.AngleData
import com.guicarneirodev.goniometro.presentation.ui.components.BackButton
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResultsScreen(navController: NavController, userId: String, patientId: String) {
    val viewModel: ResultsScreenViewModel = getViewModel { parametersOf(userId, patientId) }
    val angles by viewModel.angles.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var currentlyEditing by remember { mutableStateOf<AngleData?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    val filteredAngles = remember(angles, searchQuery) {
        if (searchQuery.isEmpty()) {
            angles
        } else {
            angles.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            ResultsAppBar(
                navController = navController,
                onAddAngle = viewModel::addAngle,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery
            )
        }
    ) { innerPadding ->
        ResultsContent(
            modifier = Modifier.padding(innerPadding),
            angles = filteredAngles,
            onEditAngle = { angle ->
                currentlyEditing = angle
                showEditDialog = true
            },
            onDeleteAngle = viewModel::deleteAngle
        )
    }

    if (showEditDialog) {
        currentlyEditing?.let { angle ->
            EditAngleDialog(
                initialName = angle.name,
                initialValue = angle.value,
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

@Composable
fun ResultsContent(
    modifier: Modifier = Modifier,
    angles: List<AngleData>,
    onEditAngle: (AngleData) -> Unit,
    onDeleteAngle: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFCBE3FF),
                        Color(0xFFCBE3FF)
                    )
                )
            )
    ) {
        LazyColumn {
            items(
                items = angles,
                key = { angle -> angle.id }
            ) { angle ->
                AngleItem(
                    angle = angle,
                    onEdit = { onEditAngle(angle) },
                    onDelete = { onDeleteAngle(angle.id) }
                )
            }
        }
    }
}

@Composable
fun AngleItem(
    angle: AngleData,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${angle.name}: ${angle.value}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onEdit) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Edit",
                tint = Color(0xFF000000)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete",
                tint = Color(0xFF000000)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsAppBar(
    navController: NavController,
    onAddAngle: (String, String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            SearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("searchField")
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE6E6E6)),
        navigationIcon = {
            BackButton(onClick = { navController.popBackStack() })
        },
        actions = {
            AddButton(onClick = { showAddDialog = true })
        }
    )

    if (showAddDialog) {
        AddAngleDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, value ->
                onAddAngle(name, value)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Buscar articulação") },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        })
    )
}

@Composable
fun AddButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "Add",
            tint = Color(0xFF000000)
        )
    }
}

@Composable
fun AddAngleDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var articulationName by remember { mutableStateOf("") }
    var angleValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Articulação") },
        text = {
            Column {
                OutlinedTextField(
                    value = articulationName,
                    onValueChange = { articulationName = it },
                    label = { Text("Nome da articulação") }
                )
                OutlinedTextField(
                    value = angleValue,
                    onValueChange = { newValue ->
                        angleValue = formatAngleValue(newValue)
                    },
                    label = { Text("Valor encontrado") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (articulationName.isNotBlank() && angleValue.isNotBlank()) {
                        onConfirm(articulationName, angleValue)
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditAngleDialog(
    initialName: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var editName by remember { mutableStateOf(initialName) }
    var editValue by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Goniometria") },
        text = {
            Column {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Nome da articulação") }
                )
                OutlinedTextField(
                    value = editValue,
                    onValueChange = { newValue ->
                        editValue = formatAngleValue(newValue)
                    },
                    label = { Text("Valor encontrado") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (editName.isNotBlank() && editValue.isNotBlank()) {
                        onConfirm(editName, editValue)
                    }
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun formatAngleValue(value: String): String {
    val cleanedValue = value.replace("°", "")
    return cleanedValue.takeIf { it.isNotEmpty() }?.let { "$it°" } ?: ""
}