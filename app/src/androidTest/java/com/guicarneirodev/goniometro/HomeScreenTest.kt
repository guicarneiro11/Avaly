package com.guicarneirodev.goniometro

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.presentation.ui.components.LoginButton
import com.guicarneirodev.goniometro.presentation.ui.components.RegisterButton
import com.guicarneirodev.goniometro.presentation.ui.components.WelcomeText
import com.guicarneirodev.goniometro.presentation.ui.screens.home.HomeContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private lateinit var mockNavController: NavController

    @Before
    fun setup() {
        mockNavController = mock(NavController::class.java)
    }

    @Test
    fun layoutStateTest() {
        rule.setContent {
            HomeContent(navController = mockNavController)
        }

        rule.onNodeWithTag("welcomeText").assertIsDisplayed()
        rule.onNodeWithTag("loginButton").assertIsDisplayed()
        rule.onNodeWithTag("registerButton").assertIsDisplayed()
    }

    @Test
    fun welcomeTextTest() {
        rule.setContent {
            WelcomeText()
        }

        rule.onNodeWithTag("welcomeText")
            .assertIsDisplayed()
    }

    @Test
    fun loginButtonTest() {
        rule.setContent {
            LoginButton(navController = mockNavController)
        }

        rule.onNodeWithTag("loginButton")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun registerButtonTest() {
        rule.setContent {
            RegisterButton(navController = mockNavController)
        }

        rule.onNodeWithTag("registerButton")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
    }
}