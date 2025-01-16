package com.guicarneirodev.goniometro

import com.guicarneirodev.goniometro.domain.validator.RegisterValidator
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class RegisterValidatorTest {
    private lateinit var validator: RegisterValidator

    @Before
    fun setup() {
        validator = RegisterValidator()
    }

    @Test
    fun emailValidator_ValidEmails_ReturnsTrue() {
        val validEmails = listOf(
            "test@example.com",
            "user.name@domain.com",
            "user+label@domain.com",
            "123@domain.com"
        )

        validEmails.forEach { email ->
            assertTrue(validator.isEmailValid(email))
        }
    }

    @Test
    fun emailValidator_InvalidEmails_ReturnsFalse() {
        val invalidEmails = listOf(
            "test@",
            "@domain.com",
            "test@domain",
            "test.domain.com",
            "",
            "test@domain..com"
        )

        invalidEmails.forEach { email ->
            assertFalse(validator.isEmailValid(email))
        }
    }

    @Test
    fun passwordValidator_ValidPasswords_ReturnsTrue() {
        val validPasswords = listOf(
            "Test1234!",
            "Password123#",
            "Complex1@Pass"
        )

        validPasswords.forEach { password ->
            assertTrue(validator.isPasswordValid(password))
        }
    }

    @Test
    fun passwordValidator_NoNumbers_ReturnsFalse() {
        // Senha com maiúscula, minúscula e caractere especial, mas sem número
        val passwordWithoutNumber = "NoNumbers!"
        assertFalse(
            "A senha '$passwordWithoutNumber' não contém números e deveria ser inválida",
            validator.isPasswordValid(passwordWithoutNumber)
        )

        // Teste adicional com outra senha sem números
        val anotherPasswordWithoutNumber = "TesteSenha@"
        assertFalse(
            "A senha '$anotherPasswordWithoutNumber' não contém números e deveria ser inválida",
            validator.isPasswordValid(anotherPasswordWithoutNumber)
        )
    }

    @Test
    fun passwordMatch_SamePasswords_ReturnsEmptyString() {
        val password = "Test1234!"
        val confirmPassword = "Test1234!"

        assertTrue(validator.passwordMatchError(password, confirmPassword).isEmpty())
    }

    @Test
    fun passwordMatch_DifferentPasswords_ReturnsErrorMessage() {
        val password = "Test1234!"
        val confirmPassword = "DifferentPass1234!"

        assertEquals(
            "As senhas não coincidem.",
            validator.passwordMatchError(password, confirmPassword)
        )
    }

    @Test
    fun passwordError_TooShort_ReturnsAppropriateMessage() {
        val password = "Sh1!"

        assertEquals(
            "A senha deve ter pelo menos 6 caracteres.",
            validator.getPasswordError(password)
        )
    }

    @Test
    fun passwordError_NoLowerCase_ReturnsAppropriateMessage() {
        val password = "TESTE123!"

        assertEquals(
            "A senha deve conter pelo menos uma letra minúscula.",
            validator.getPasswordError(password)
        )
    }

    @Test
    fun passwordError_NoUpperCase_ReturnsAppropriateMessage() {
        val password = "teste123!"

        assertEquals(
            "A senha deve conter pelo menos uma letra maiúscula.",
            validator.getPasswordError(password)
        )
    }

    @Test
    fun passwordError_NoNumber_ReturnsAppropriateMessage() {
        val password = "TesteSenha!"

        assertEquals(
            "A senha deve conter pelo menos um número.",
            validator.getPasswordError(password)
        )
    }

    @Test
    fun passwordError_NoSpecialChar_ReturnsAppropriateMessage() {
        val password = "Teste123"

        assertEquals(
            "A senha deve conter pelo menos um caractere especial.",
            validator.getPasswordError(password)
        )
    }

    @Test
    fun passwordError_ValidPassword_ReturnsEmptyString() {
        val password = "Teste123!"

        assertTrue(validator.getPasswordError(password).isEmpty())
    }
}