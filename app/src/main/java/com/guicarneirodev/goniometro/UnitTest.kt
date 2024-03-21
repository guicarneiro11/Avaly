package com.guicarneirodev.goniometro

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ValidViewModelTest {

    private val validViewModel = ValidViewModel()

    @Test
    fun testisEmailValid() {
        Assertions.assertTrue(validViewModel.isEmailValid("test@example.com"))

        Assertions.assertFalse(validViewModel.isEmailValid("invalid_email.com"))
    }

    @Test
    fun testisPasswordValid() {
        Assertions.assertTrue(validViewModel.isPasswordValid("Password123!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Pass!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("PASSWORD123!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("password123!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Password!"))

        Assertions.assertFalse(validViewModel.isPasswordValid("Password123"))
    }
}