package com.guicarneirodev.goniometro.presentation.ui.screens.results

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.repository.AngleData
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
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
        containerColor = Color(0xFF1E88E5)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E88E5),
                            Color(0xFF4FC3F7)
                        )
                    )
                )
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

@Composable
fun ModernEditDialog(
    angle: AngleData,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var editName by remember { mutableStateOf(angle.name) }
    var editValue by remember { mutableStateOf(angle.value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "Editar Goniometria",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Nome da articulação") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E88E5),
                        focusedLabelColor = Color(0xFF1E88E5),
                        cursorColor = Color(0xFF1E88E5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = editValue,
                    onValueChange = { newValue ->
                        editValue = formatAngleValue(newValue)
                    },
                    label = { Text("Valor encontrado") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E88E5),
                        focusedLabelColor = Color(0xFF1E88E5),
                        cursorColor = Color(0xFF1E88E5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (editName.isNotBlank() && editValue.isNotBlank()) {
                        onConfirm(editName, editValue)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Salvar",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1E88E5)
                ),
                border = BorderStroke(1.dp, Color(0xFF1E88E5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Cancelar",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
fun ModernAddDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var articulationName by remember { mutableStateOf("") }
    var angleValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "Adicionar Goniometria",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = articulationName,
                    onValueChange = { articulationName = it },
                    label = { Text("Nome da articulação") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E88E5),
                        focusedLabelColor = Color(0xFF1E88E5),
                        cursorColor = Color(0xFF1E88E5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = angleValue,
                    onValueChange = { newValue ->
                        angleValue = formatAngleValue(newValue)
                    },
                    label = { Text("Valor encontrado") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E88E5),
                        focusedLabelColor = Color(0xFF1E88E5),
                        cursorColor = Color(0xFF1E88E5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (articulationName.isNotBlank() && angleValue.isNotBlank()) {
                        onConfirm(articulationName, angleValue)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Adicionar",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1E88E5)
                ),
                border = BorderStroke(1.dp, Color(0xFF1E88E5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Cancelar",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
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
    onAddAngle: (String, String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Resultados", color = Color(0xFF1E88E5)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color(0xFF1E88E5)
                )
            }
        },
        actions = {
            IconButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Adicionar",
                    tint = Color(0xFF1E88E5)
                )
            }
        }
    )

    if (showAddDialog) {
        ModernAddDialog(
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFF1E88E5),
            unfocusedBorderColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        placeholder = {
            Text("Buscar articulação", color = Color.Gray)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color(0xFF1E88E5)
            )
        },
        singleLine = true
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
fun ResultsList(
    angles: List<AngleData>,
    searchQuery: String,
    onEditAngle: (AngleData) -> Unit,
    onDeleteAngle: (String) -> Unit
) {
    val filteredAngles = remember(angles, searchQuery) {
        if (searchQuery.isEmpty()) {
            angles
        } else {
            angles.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.value.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filteredAngles, key = { it.id }) { angle ->
            AngleCard(
                angle = angle,
                onEdit = { onEditAngle(angle) },
                onDelete = { onDeleteAngle(angle.id) }
            )
        }
    }
}

@Composable
fun AngleCard(
    angle: AngleData,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = angle.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1E88E5)
                )
                Text(
                    text = angle.value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFF1E88E5).copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Editar",
                        tint = Color(0xFF1E88E5)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFFE57373).copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Excluir",
                        tint = Color(0xFFE57373)
                    )
                }
            }
        }
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