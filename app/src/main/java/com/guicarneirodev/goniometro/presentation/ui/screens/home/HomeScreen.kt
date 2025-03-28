package com.guicarneirodev.goniometro.presentation.ui.screens.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.ui.reusable.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.HomeContent
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.LogoSection
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryDark)
    ) {
        BackgroundDecorations()

        LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("home_content"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(80.dp))
                    LogoSection()
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    HomeContent(navController)
                }
            }
        }
    }
}