package com.guicarneirodev.goniometro

import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ValidViewModelTest {

    private val validViewModel = ValidViewModel()

    @Test
    fun testEmailValid() {
        Assertions.assertTrue(validViewModel.isEmailValid("test@example.com"))

        Assertions.assertFalse(validViewModel.isEmailValid("invalid_email.com"))

        Assertions.assertFalse(validViewModel.isEmailValid("invalid_email@com"))
    }

    @Test
    fun testPasswordValid() {
        Assertions.assertTrue(validViewModel.isPasswordValid("Abc123!"))

        Assertions.assertTrue(validViewModel.isPasswordValid("Abcdef123%#"))

        Assertions.assertFalse(validViewModel.isPasswordValid("abc123!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("A23456!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("123456!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Abcdef!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Abc123"))

        Assertions.assertFalse(validViewModel.isPasswordValid("abc123"))

        Assertions.assertFalse(validViewModel.isPasswordValid("123456"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Abcdef"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Abc!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("123!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Abc123"))
    }
}