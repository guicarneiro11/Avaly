package com.guicarneirodev.goniometro

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.presentation.ui.screens.home.HomeScreen
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @RelaxedMockK
    private lateinit var navController: NavController
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun verifyVisualElements() {
        composeTestRule.setContent {
            HomeScreen(navController = navController)
        }

        val logoNode = composeTestRule
            .onNodeWithContentDescription("Avaly Logo")

        val loginButton = composeTestRule
            .onNodeWithTag("login_button")

        val registerButton = composeTestRule
            .onNodeWithTag("register_button")

        composeTestRule.assertVisualElementsInOrder(
            logoNode,
            loginButton,
            registerButton
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenLoginButtonClicked_navigatesToLoginScreen() {
        composeTestRule.setContent {
            HomeScreen(navController = navController)
        }

        composeTestRule
            .onNodeWithTag("login_button")
            .performClick()

        testDispatcher.scheduler.advanceTimeBy(1000)

        verify { navController.navigate("login") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenRegisterButtonClicked_navigatesToRegisterScreen() {
        composeTestRule.setContent {
            HomeScreen(navController = navController)
        }

        composeTestRule
            .onNodeWithTag("register_button")
            .performClick()

        testDispatcher.scheduler.advanceTimeBy(1000)

        verify { navController.navigate("register") }
    }
}