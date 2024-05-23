package com.guicarneirodev.goniometro

import org.junit.jupiter.api.Assertions
import org.junit.Test

class ValidViewModelTest {

    private val validViewModel = ValidViewModel()

    @Test
    fun testisEmailValid() {
        Assertions.assertTrue(validViewModel.isEmailValid("test@example.com"))

        Assertions.assertFalse(validViewModel.isEmailValid("invalid_email.com"))

        Assertions.assertFalse(validViewModel.isEmailValid("invalid_email@com"))
    }

    @Test
    fun testisPasswordValid() {
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