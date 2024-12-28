package com.guicarneirodev.goniometro.presentation.ui.screens.patients.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.Patient

@Composable
fun PatientCard(
    patient: Patient,
    onEdit: (Patient) -> Unit,
    onDelete: (String) -> Unit,
    onSendPdf: (String) -> Unit,
    onNavigateToResults: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = patient.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF1E88E5)
                    )
                    Text(
                        text = patient.evaluationDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1E88E5).copy(alpha = 0.7f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionIconButton(
                        icon = R.drawable.reportpdf,
                        description = stringResource(R.string.pdf),
                        onClick = { onSendPdf(patient.id) }
                    )
                    ActionIconButton(
                        icon = R.drawable.edit,
                        description = stringResource(R.string.edit),
                        onClick = { onEdit(patient) }
                    )
                    ActionIconButton(
                        icon = R.drawable.delete,
                        description = stringResource(R.string.delete),
                        onClick = { onDelete(patient.id) }
                    )
                    ActionIconButton(
                        icon = R.drawable.arrow,
                        description = stringResource(R.string.results),
                        onClick = { onNavigateToResults(patient.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ActionIconButton(
    icon: Int,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .background(
                color = Color(0xFF1E88E5).copy(alpha = 0.1f),
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = description,
            tint = Color(0xFF1E88E5),
            modifier = Modifier.size(24.dp)
        )
    }
}