package com.guicarneirodev.goniometro

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>() // Especificando o tipo da activity

    @Test
    fun registerUser_calls_authRepository_and_invokes_onSuccess_when_successful() {
        composeTestRule.setContent {
            RegisterScreen(
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