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
        Assertions.assertTrue(validViewModel.isPasswordValid("123456"))

        Assertions.assertFalse(validViewModel.isPasswordValid("1"))

        Assertions.assertFalse(validViewModel.isPasswordValid("12"))

        Assertions.assertFalse(validViewModel.isPasswordValid("123"))

        Assertions.assertFalse(validViewModel.isPasswordValid("1234"))

        Assertions.assertFalse(validViewModel.isPasswordValid("12345"))
    }
}