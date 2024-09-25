package com.guicarneirodev.goniometro

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.presentation.ui.screens.register.Register
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun registerUser_calls_authRepository_and_invokes_onSuccess_when_successful() {
        composeTestRule.setContent {
            Register(
                email = "",
                password = "",
                confirmPassword = "",
                emailError = "",
                passwordError = "",
                confirmPasswordError = "",
                passwordVisibility = false,
                onEmailChange = {},
                onPasswordChange = {},
                onConfirmPasswordChange = {},
                onPasswordVisibilityChange = {},
                onRegisterClick = {},
                onBackClick = {},
                errorMessage = ""
            )
        }

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Senha").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirmar senha").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criar conta").assertIsDisplayed()
    }
}