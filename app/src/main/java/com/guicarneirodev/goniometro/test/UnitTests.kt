package com.guicarneirodev.goniometro.test

import com.guicarneirodev.goniometro.domain.validator.RegisterValidator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class RegisterValidatorTest {

    private lateinit var registerValidator: RegisterValidator

    @Before
    fun setup() {
        registerValidator = RegisterValidator()
    }

    @Test
    fun isEmailValidReturnsTrueForValidEmail() {
        assertTrue(registerValidator.isEmailValid("test@example.com"))
        assertTrue(registerValidator.isEmailValid("user.name+tag@example.co.uk"))
    }

    @Test
    fun isEmailValidReturnsFalseForInvalidEmail() {
        assertFalse(registerValidator.isEmailValid("invalid_email.com"))
        assertFalse(registerValidator.isEmailValid("invalid_email@com"))
        assertFalse(registerValidator.isEmailValid("@example.com"))
    }

    @Test
    fun isPasswordValidReturnsTrueForValidPassword() {
        assertTrue(registerValidator.isPasswordValid("Abc123!"))
        assertTrue(registerValidator.isPasswordValid("P@ssw0rd"))
        assertTrue(registerValidator.isPasswordValid("C0mpl3x!Pass"))
    }

    @Test
    fun isPasswordValidReturnsFalseForInvalidPassword() {
        assertFalse(registerValidator.isPasswordValid("abc123!"))
        assertFalse(registerValidator.isPasswordValid("ABC123!"))
        assertFalse(registerValidator.isPasswordValid("Abcdef!"))
        assertFalse(registerValidator.isPasswordValid("Abc123"))
        assertFalse(registerValidator.isPasswordValid("Ab1!"))
    }

    @Test
    fun passwordMatchErrorReturnsErrorMessageWhenPasswordsDontMatch() {
        assertEquals("As senhas não coincidem.", registerValidator.passwordMatchError("password1", "password2"))
    }

    @Test
    fun passwordMatchErrorReturnsEmptyStringWhenPasswordsMatch() {
        assertEquals("", registerValidator.passwordMatchError("password", "password"))
    }

    @Test
    fun getPasswordErrorReturnsAppropriateErrorMessages() {
        assertEquals("A senha deve ter pelo menos 6 caracteres.", registerValidator.getPasswordError("Abc1!"))
        assertEquals("A senha deve conter pelo menos uma letra minúscula.", registerValidator.getPasswordError("ABC123!"))
        assertEquals("A senha deve conter pelo menos uma letra maiúscula.", registerValidator.getPasswordError("abc123!"))
        assertEquals("A senha deve conter pelo menos um número.", registerValidator.getPasswordError("Abcdef!"))
        assertEquals("A senha deve conter pelo menos um caractere especial.", registerValidator.getPasswordError("Abcdef123"))
        assertEquals("", registerValidator.getPasswordError("ValidP@ss1"))
    }
}