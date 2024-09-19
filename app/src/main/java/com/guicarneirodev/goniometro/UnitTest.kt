import com.guicarneirodev.goniometro.AuthRepository
import com.guicarneirodev.goniometro.RegisterViewModel
import com.guicarneirodev.goniometro.Validator
import org.mockito.Mock
import org.mockito.Mockito.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class ValidatorTest {

    private lateinit var validator: Validator

    @Before
    fun setup() {
        validator = Validator()
    }

    @Test
    fun isEmailValid_returnsTrue_forValidEmail() {
        assertTrue(validator.isEmailValid("test@example.com"))
        assertTrue(validator.isEmailValid("user.name+tag@example.co.uk"))
    }

    @Test
    fun isEmailValid_returnsFalse_forInvalidEmail() {
        assertFalse(validator.isEmailValid("invalid_email.com"))
        assertFalse(validator.isEmailValid("invalid_email@com"))
        assertFalse(validator.isEmailValid("@example.com"))
    }

    @Test
    fun isPasswordValid_returns_true_for_valid_password() {
        assertTrue(validator.isPasswordValid("Abc123!"))
        assertTrue(validator.isPasswordValid("P@ssw0rd"))
        assertTrue(validator.isPasswordValid("C0mpl3x!Pass"))
    }

    @Test
    fun isPasswordValid_returns_false_for_invalid_password() {
        assertFalse(validator.isPasswordValid("abc123!"))
        assertFalse(validator.isPasswordValid("ABC123!"))
        assertFalse(validator.isPasswordValid("Abcdef!"))
        assertFalse(validator.isPasswordValid("Abc123"))
        assertFalse(validator.isPasswordValid("Ab1!"))
    }

    @Test
    fun passwordMatchError_returns_error_message_when_passwords_dont_match() {
        assertEquals("As senhas não coincidem.", validator.passwordMatchError("password1", "password2"))
    }

    @Test
    fun passwordMatchError_returns_empty_string_when_passwords_match() {
        assertEquals("", validator.passwordMatchError("password", "password"))
    }

    @Test
    fun getPasswordError_returns_appropriate_error_messages() {
        assertEquals("A senha deve ter pelo menos 6 caracteres.", validator.getPasswordError("Abc1!"))
        assertEquals("A senha deve conter pelo menos uma letra minúscula.", validator.getPasswordError("ABC123!"))
        assertEquals("A senha deve conter pelo menos uma letra maiúscula.", validator.getPasswordError("abc123!"))
        assertEquals("A senha deve conter pelo menos um número.", validator.getPasswordError("Abcdef!"))
        assertEquals("A senha deve conter pelo menos um caractere especial.", validator.getPasswordError("Abcdef123"))
        assertEquals("", validator.getPasswordError("ValidP@ss1"))
    }
}