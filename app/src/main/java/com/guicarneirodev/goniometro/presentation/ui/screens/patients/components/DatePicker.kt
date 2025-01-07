package com.guicarneirodev.goniometro.presentation.ui.screens.patients.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun DatePicker(
    onDateSelected: (String) -> Unit,
    initialDate: String? = null
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(initialDate ?: "", TextRange(0)))
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val digitsOnly = newValue.text.filter { it.isDigit() }
            if (digitsOnly.length <= 8) {
                val formatted = formatDateInput(digitsOnly)
                val newCursor = if (formatted.length > textFieldValue.text.length) {
                    newValue.selection.start + 1
                } else {
                    newValue.selection.start
                }
                textFieldValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(newCursor)
                )
                if (digitsOnly.length == 8) {
                    onDateSelected(formatted)
                }
            }
        },
        label = { Text(stringResource(R.string.evaluation_date)) },
        placeholder = { Text(stringResource(R.string.date_placeholder)) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentBlue,
            focusedLabelColor = AccentBlue,
            unfocusedBorderColor = SecondaryDark.copy(alpha = 0.3f),
            unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

private fun formatDateInput(input: String): String {
    return buildString {
        input.forEachIndexed { index, char ->
            if (index == 2 || index == 4) append('/')
            append(char)
        }
    }
}