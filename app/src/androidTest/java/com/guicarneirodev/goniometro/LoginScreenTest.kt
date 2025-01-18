package com.guicarneirodev.goniometro

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.domain.repository.fake.FakeLoginRepository
import com.guicarneirodev.goniometro.domain.repository.fake.FakePreferencesRepository
import com.guicarneirodev.goniometro.presentation.ui.screens.login.LoginScreen
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.fake.FakeLoginViewModel
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @RelaxedMockK
    private lateinit var navController: NavController

    private lateinit var navOptionsSlot: CapturingSlot<NavOptions>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        navOptionsSlot = slot()
        every {
            navController.navigate(
                route = any(),
                navOptions = capture(navOptionsSlot)
            )
        } just Runs

        every { navController.graph } returns mockk {
            every { startDestinationId } returns 1
        }
    }

    @Test
    fun whenScreenStarts_showsAllElements() {
        // Arrange & Act
        composeTestRule.setContent {
            LoginScreen(navController = navController)
        }

        // Assert
        composeTestRule
            .onNodeWithTag("email_field")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("password_field")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("remember_email_checkbox")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("login_button")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun whenLoading_showsLoadingIndicator() {
        // Arrange
        composeTestRule.setContent {
            val testViewModel = FakeLoginViewModel()
            LaunchedEffect(Unit) {
                testViewModel.setLoading(true)
            }
            LoginScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        // Assert
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("loading_indicator")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun whenLoginSucceeds_navigatesToSelection() {
        // Arrange
        composeTestRule.setContent {
            val testViewModel = FakeLoginViewModel()
            LaunchedEffect(Unit) {
                testViewModel.simulateSuccessfulLogin()
            }
            LoginScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        // Act
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            verify(timeout = 1000) {
                navController.navigate(
                    route = eq("selection"),
                    navOptions = any()
                )
            }
            true
        }

        // Wait for UI update and navigation
        composeTestRule.waitForIdle()

        // Assert
        verify(timeout = 1000) {
            navController.navigate(
                route = "selection",
                navOptions = any()
            )
        }
    }

    @Test
    fun whenForgotPasswordClicked_showsResetPasswordFields() {
        // Arrange
        composeTestRule.setContent {
            LoginScreen(navController = navController)
        }

        // Act
        composeTestRule
            .onNodeWithTag("forgot_password_button")
            .performClick()

        // Wait for UI update
        composeTestRule.waitForIdle()

        // Assert
        composeTestRule
            .onNodeWithTag("reset_email_field")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("send_code_button")
            .assertExists()
            .assertIsDisplayed()
    }
}
