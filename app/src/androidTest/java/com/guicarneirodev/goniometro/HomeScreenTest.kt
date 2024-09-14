package com.guicarneirodev.goniometro

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    fun dragGestureTest() {
        rule.setContent {
            Home(navController = mockNavController)
        }

        val box = rule.onNodeWithTag("homeBackground").assertIsDisplayed()
        box.performTouchInput { swipeUp() }
    }

    @Test
    fun welcomeTextTest() {
        rule.setContent {
            Home(navController = mockNavController)
        }

        rule.onNodeWithText("Bem-vindo ao\nAngle Pro,\nsua goniometria\nem poucos cliques.")
            .assertIsDisplayed()
    }

    @Test
    fun layoutStateTest() {
        rule.setContent {
            Home(navController = mockNavController)
        }

        rule.onNodeWithTag("loginButton").assertIsDisplayed()
        rule.onNodeWithTag("registerButton").assertIsDisplayed()

        rule.onNodeWithText("Fazer Login")
            .assertIsDisplayed()
        rule.onNodeWithText("Criar Conta")
            .assertIsDisplayed()
    }

    @Test
    fun loginButtonTest() {
        rule.setContent {
            Home(navController = mockNavController)
        }

        rule.onNodeWithTag("loginButton")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun registerButtonTest() {
        rule.setContent {
            Home(navController = mockNavController)
        }

        rule.onNodeWithTag("registerButton")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
    }
}