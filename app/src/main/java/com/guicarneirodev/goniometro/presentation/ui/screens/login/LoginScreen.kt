package com.guicarneirodev.goniometro.presentation.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.factory.LoginViewModelFactory
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.login.components.LoginCard
import com.guicarneirodev.goniometro.presentation.ui.screens.login.components.LoginHeader
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginScreenViewModel = viewModel(factory = LoginViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToSelection) {
        if (uiState.navigateToSelection) {
            navController.navigate("selection") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5), Color(0xFF4FC3F7)
                    )
                )
            )
    ) {
        BackgroundDecorations()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginHeader(showResetPassword = uiState.showResetPassword, onBackClick = {
                    if (uiState.showResetPassword) {
                        viewModel.onBackToLoginClick()
                    } else {
                        navController.navigate("selection") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                })

                Spacer(modifier = Modifier.height(24.dp))

                LoginCard(
                    uiState = uiState,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onRememberEmailChange = viewModel::onRememberEmailChange,
                    onRememberPasswordChange = viewModel::onRememberPasswordChange,
                    onLoginClick = viewModel::onLoginClick,
                    onResetPasswordClick = viewModel::onResetPasswordClick,
                    onSendResetCodeClick = viewModel::onSendResetCodeClick,
                    onVerifyResetCodeClick = viewModel::onVerifyResetCodeClick,
                    onSecurityCodeChange = viewModel::onSecurityCodeChange
                )
            }
        }
    }
}