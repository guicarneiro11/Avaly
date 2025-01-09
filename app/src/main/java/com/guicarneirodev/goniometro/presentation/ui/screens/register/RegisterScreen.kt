package com.guicarneirodev.goniometro.presentation.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.ui.reusable.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.register.components.RegisterCard
import com.guicarneirodev.goniometro.presentation.ui.screens.register.components.RegisterHeader
import com.guicarneirodev.goniometro.presentation.viewmodel.RegisterScreenViewModel
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryDark)
    ) {
        BackgroundDecorations()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RegisterHeader(
                        onBackClick = { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RegisterCard(
                        uiState = uiState,
                        onEmailChange = viewModel::updateEmail,
                        onPasswordChange = viewModel::updatePassword,
                        onConfirmPasswordChange = viewModel::updateConfirmPassword,
                        onRegisterClick = {
                            viewModel.registerUser(
                                onSuccess = { navController.navigate("home") },
                                onError = { /* Erro já mostrado no card */ }
                            )
                        }
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SecondaryDark.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AccentBlue,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}