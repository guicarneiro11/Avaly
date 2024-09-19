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
    fun `isPasswordValid returns true for valid password`() {
        assertTrue(validator.isPasswordValid("Abc123!"))
        assertTrue(validator.isPasswordValid("P@ssw0rd"))
        assertTrue(validator.isPasswordValid("C0mpl3x!Pass"))
    }

    @Test
    fun `isPasswordValid returns false for invalid password`() {
        assertFalse(validator.isPasswordValid("abc123!"))
        assertFalse(validator.isPasswordValid("ABC123!"))
        assertFalse(validator.isPasswordValid("Abcdef!"))
        assertFalse(validator.isPasswordValid("Abc123"))
        assertFalse(validator.isPasswordValid("Ab1!"))
    }

    @Test
    fun `passwordMatchError returns error message when passwords don't match`() {
        assertEquals("As senhas não coincidem.", validator.passwordMatchError("password1", "password2"))
    }

    @Test
    fun `passwordMatchError returns empty string when passwords match`() {
        assertEquals("", validator.passwordMatchError("password", "password"))
    }

    @Test
    fun `getPasswordError returns appropriate error messages`() {
        assertEquals("A senha deve ter pelo menos 6 caracteres.", validator.getPasswordError("Abc1!"))
        assertEquals("A senha deve conter pelo menos uma letra minúscula.", validator.getPasswordError("ABC123!"))
        assertEquals("A senha deve conter pelo menos uma letra maiúscula.", validator.getPasswordError("abc123!"))
        assertEquals("A senha deve conter pelo menos um número.", validator.getPasswordError("Abcdef!"))
        assertEquals("A senha deve conter pelo menos um caractere especial.", validator.getPasswordError("Abcdef123"))
        assertEquals("", validator.getPasswordError("ValidP@ss1"))
    }
}

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @Mock
    private lateinit var validator: Validator

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        viewModel = RegisterViewModel(validator, authRepository)
    }

    @Test
    fun updateEmail_updatesEmailState_andValidatesEmail() {
        `when`(validator.isEmailValid("test@example.com")).thenReturn(true)

        viewModel.updateEmail("test@example.com")

        assertEquals("test@example.com", viewModel.email.value)
        assertEquals("", viewModel.emailError.value)

        verify(validator).isEmailValid("test@example.com")
    }

    @Test
    fun `updateEmail sets error for invalid email`() {
        `when`(validator.isEmailValid("invalid_email")).thenReturn(false)

        viewModel.updateEmail("invalid_email")

        assertEquals("invalid_email", viewModel.email.value)
        assertEquals("Email inválido", viewModel.emailError.value)

        verify(validator).isEmailValid("invalid_email")
    }

    @Test
    fun `updatePassword updates password state and validates password`() {
        `when`(validator.getPasswordError("ValidP@ss1")).thenReturn("")

        viewModel.updatePassword("ValidP@ss1")

        assertEquals("ValidP@ss1", viewModel.password.value)
        assertEquals("", viewModel.passwordError.value)

        verify(validator).getPasswordError("ValidP@ss1")
    }

    @Test
    fun `updatePassword sets error for invalid password`() {
        `when`(validator.getPasswordError("weak")).thenReturn("A senha deve ter pelo menos 6 caracteres.")

        viewModel.updatePassword("weak")

        assertEquals("weak", viewModel.password.value)
        assertEquals("A senha deve ter pelo menos 6 caracteres.", viewModel.passwordError.value)

        verify(validator).getPasswordError("weak")
    }

    @Test
    fun `updateConfirmPassword updates confirmPassword state and validates match`() {
        viewModel.updatePassword("ValidP@ss1")
        `when`(validator.passwordMatchError("ValidP@ss1", "ValidP@ss1")).thenReturn("")

        viewModel.updateConfirmPassword("ValidP@ss1")

        assertEquals("ValidP@ss1", viewModel.confirmPassword.value)
        assertEquals("", viewModel.confirmPasswordError.value)

        verify(validator).passwordMatchError("ValidP@ss1", "ValidP@ss1")
    }

    @Test
    fun `updateConfirmPassword sets error when passwords don't match`() {
        viewModel.updatePassword("ValidP@ss1")
        `when`(validator.passwordMatchError("ValidP@ss1", "DifferentP@ss1")).thenReturn("As senhas não coincidem.")

        viewModel.updateConfirmPassword("DifferentP@ss1")

        assertEquals("DifferentP@ss1", viewModel.confirmPassword.value)
        assertEquals("As senhas não coincidem.", viewModel.confirmPasswordError.value)

        verify(validator).passwordMatchError("ValidP@ss1", "DifferentP@ss1")
    }

    @Test
    suspend fun `registerUser calls authRepository and invokes onSuccess when successful`() {
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("ValidP@ss1")
        viewModel.updateConfirmPassword("ValidP@ss1")

        `when`(authRepository.registerUser("test@example.com", "ValidP@ss1")).thenReturn(Result.success(Unit))

        var successCalled = false
        var errorMessage: String? = null

        viewModel.registerUser(
            onSuccess = { successCalled = true },
            onError = { errorMessage = it }
        )

        assertTrue(successCalled)
        assertNull(errorMessage)

        verify(authRepository).registerUser("test@example.com", "ValidP@ss1")
    }

    @Test
    fun registerUser_callsAuthRepository_andInvokesOnSuccess_whenSuccessful() {
        runBlocking {
            viewModel.updateEmail("test@example.com")
            viewModel.updatePassword("ValidP@ss1")
            viewModel.updateConfirmPassword("ValidP@ss1")

            `when`(authRepository.registerUser("test@example.com", "ValidP@ss1")).thenReturn(Result.success(Unit))

            var successCalled = false
            var errorMessage: String? = null

            viewModel.registerUser(
                onSuccess = { successCalled = true },
                onError = { errorMessage = it }
            )

            assertTrue(successCalled)
            assertNull(errorMessage)

            verify(authRepository).registerUser("test@example.com", "ValidP@ss1")
        }
    }
}