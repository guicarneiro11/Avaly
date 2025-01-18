package com.guicarneirodev.goniometro

import com.guicarneirodev.goniometro.domain.repository.LoginPreferencesRepository
import com.guicarneirodev.goniometro.domain.repository.LoginRepository
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import io.mockk.*
import kotlinx.coroutines.test.advanceTimeBy

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginScreenViewModel
    private lateinit var loginRepository: LoginRepository
    private lateinit var preferencesRepository: LoginPreferencesRepository

    @Before
    fun setup() {
        loginRepository = mockk<LoginRepository>()
        preferencesRepository = mockk<LoginPreferencesRepository>(relaxed = true)

        every { preferencesRepository.getEmail() } returns ""
        every { preferencesRepository.getPassword() } returns ""
        every { preferencesRepository.getRememberEmail() } returns false
        every { preferencesRepository.getRememberPassword() } returns false

        viewModel = LoginScreenViewModel(loginRepository, preferencesRepository)
    }

    @Test
    fun `when initial state, all fields are empty and remember options are false`() = runTest {
        assertEquals("", viewModel.uiState.value.email)
        assertEquals("", viewModel.uiState.value.password)
        assertFalse(viewModel.uiState.value.rememberEmail)
        assertFalse(viewModel.uiState.value.rememberPassword)
    }

    @Test
    fun `when login with valid credentials, navigates to selection`() = runTest {
        coEvery {
            loginRepository.signInWithEmail(any(), any())
        } returns Result.success(Unit)

        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        advanceTimeBy(500)

        assertTrue(viewModel.uiState.value.navigateToSelection)
    }

    @Test
    fun `when login fails, shows error message`() = runTest {
        coEvery {
            loginRepository.signInWithEmail(any(), any())
        } returns Result.failure(Exception("Invalid credentials"))

        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClick()

        advanceTimeBy(500)

        assertEquals("Email ou senha incorretos", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `when remember email is enabled, saves email in preferences`() = runTest {
        every { preferencesRepository.setEmail(any()) } just Runs

        viewModel.onEmailChange("test@test.com")
        viewModel.onRememberEmailChange(true)

        coEvery { loginRepository.signInWithEmail(any(), any()) } returns Result.success(Unit)
        viewModel.onLoginClick()
        advanceTimeBy(500)

        verify { preferencesRepository.setEmail("test@test.com") }
    }

    @Test
    fun `when send reset code succeeds, shows reset code field`() = runTest {
        coEvery {
            loginRepository.sendSecurityCode(any())
        } returns Result.success(Unit)

        viewModel.onEmailChange("test@test.com")
        viewModel.onResetPasswordClick()
        viewModel.onSendResetCodeClick()

        advanceTimeBy(500)

        coVerify { loginRepository.sendSecurityCode("test@test.com") }
        assertTrue(viewModel.uiState.value.showResetPassword)
        assertTrue(viewModel.uiState.value.resetCodeSent)
        assertTrue(viewModel.uiState.value.resetEmailSent)
    }

    @Test
    fun `when verify reset code succeeds, returns to login screen`() = runTest {
        coEvery {
            loginRepository.verifySecurityCode(any(), any())
        } returns Result.success(true)

        coEvery {
            loginRepository.resetPassword(any())
        } returns Result.success(Unit)

        viewModel.onEmailChange("test@test.com")
        viewModel.onResetPasswordClick()
        viewModel.onSecurityCodeChange("123456")

        viewModel.onVerifyResetCodeClick()

        advanceTimeBy(500)

        coVerify { loginRepository.verifySecurityCode("test@test.com", "123456") }
        coVerify { loginRepository.resetPassword("test@test.com") }
        assertFalse(viewModel.uiState.value.showResetPassword)
    }
}