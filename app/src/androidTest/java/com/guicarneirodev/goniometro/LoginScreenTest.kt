package com.guicarneirodev.goniometro

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    private val email = "guicarneiro.dev@gmail.com"
    private val password = "Thklabr641642!"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testSignInWithValidCredentials() {
        rule.setContent {
            val mockNavController = rememberNavController()
            val mockFirebaseAuthManager = mock(FirebaseAuthManager::class.java)

            `when`(mockFirebaseAuthManager.signInEmail(eq(email), eq(password), any())).thenAnswer {
                val callback = it.getArgument<(Result<Unit>) -> Unit>(2)
                callback(Result.success(Unit))
                null
            }

            Login(
                navController = mockNavController)
        }

        rule.onNodeWithTag("emailField").performTextInput(email)
        rule.onNodeWithTag("passwordField").performTextInput(password)
        rule.onNodeWithTag("loginButton").performClick()
    }
}