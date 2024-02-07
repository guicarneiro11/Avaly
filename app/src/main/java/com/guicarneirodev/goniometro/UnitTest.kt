package com.guicarneirodev.goniometro

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ValidViewModelTest {

    private val validViewModel = ValidViewModel()

    @Test
    fun testisEmailValid() {
        assertTrue(validViewModel.isEmailValid("test@example.com"))

        assertFalse(validViewModel.isEmailValid("invalid_email.com"))
    }

    @Test
    fun testisPasswordValid() {
        assertTrue(validViewModel.isPasswordValid("Password123!"))

        assertFalse(validViewModel.isPasswordValid("Pass!"))

        assertFalse(validViewModel.isPasswordValid("PASSWORD123!"))

        assertFalse(validViewModel.isPasswordValid("password123!"))

        assertFalse(validViewModel.isPasswordValid("Password!"))

        assertFalse(validViewModel.isPasswordValid("Password123"))
    }
}