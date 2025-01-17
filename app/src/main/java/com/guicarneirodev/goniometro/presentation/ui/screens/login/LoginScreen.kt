package com.guicarneirodev.goniometro.presentation.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.factory.LoginViewModelFactory
import com.guicarneirodev.goniometro.presentation.ui.reusable.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.login.components.LoginCard
import com.guicarneirodev.goniometro.presentation.ui.screens.login.components.LoginHeader
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToSelection) {
        if (uiState.navigateToSelection) {
            navController.navigate("selection") {
                popUpTo("login") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
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
                    LoginHeader(
                        showResetPassword = uiState.showResetPassword,
                        onBackClick = {
                            if (uiState.showResetPassword) {
                                viewModel.onBackToLoginClick()
                            } else {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )

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
}