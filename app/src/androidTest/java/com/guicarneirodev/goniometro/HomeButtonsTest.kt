package com.guicarneirodev.goniometro

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
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
    fun whenScreenStarts_allElementsAreDisplayed() {
        composeTestRule.setContent {
            HomeScreen(navController = navController)
        }

        composeTestRule
            .onNodeWithTag("navigation_buttons")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("login_button")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("register_button")
            .assertIsDisplayed()
            .assertIsEnabled()
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