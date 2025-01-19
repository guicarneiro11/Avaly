package com.guicarneirodev.goniometro

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.presentation.ui.screens.login.LoginScreen
import com.guicarneirodev.goniometro.presentation.viewmodel.fake.FakeLoginViewModel
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
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
    fun whenLoginSucceeds_navigatesToSelection() = runTest {
        // Arrange
        val testViewModel = FakeLoginViewModel()

        // Act
        composeTestRule.setContent {
            LoginScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        // Trigger login success
        testViewModel.simulateSuccessfulLogin()

        // Advance time to allow coroutines to complete
        testScheduler.advanceUntilIdle()

        // Ensure UI is updated
        composeTestRule.waitForIdle()

        // Small additional delay
        delay(500)

        with(testViewModel.uiState.value) {
            assertTrue("Navigation should be triggered", navigateToSelection)
            assertFalse("Loading state should be complete", isLoading)
            assertNull("Error message should be cleared", errorMessage)
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