package com.guicarneirodev.goniometro.presentation.ui.reusable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

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
            focusedContainerColor = PrimaryLight,
            unfocusedContainerColor = PrimaryLight,
            focusedBorderColor = AccentBlue,
            unfocusedBorderColor = PrimaryLight,
            focusedLeadingIconColor = AccentBlue,
            unfocusedLeadingIconColor = AccentBlue.copy(alpha = 0.7f),
            focusedPlaceholderColor = SecondaryDark.copy(alpha = 0.6f),
            unfocusedPlaceholderColor = SecondaryDark.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp),
        placeholder = {
            Text(stringResource(R.string.search_hint))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_hint)
            )
        },
        singleLine = true
    )
}