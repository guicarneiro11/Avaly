package com.guicarneirodev.goniometro

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.presentation.ui.screens.register.RegisterScreen
import com.guicarneirodev.goniometro.presentation.viewmodel.FakeRegisterViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @RelaxedMockK
    private lateinit var navController: NavController

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun initialState_showsEmptyForm() {
        composeTestRule.setContent {
            RegisterScreen(navController = navController)
        }

        composeTestRule
            .onNodeWithTag("email_field")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("password_field")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("confirm_password_field")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("register_button")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun whenInvalidEmail_showsError() {
        val testViewModel = FakeRegisterViewModel()

        composeTestRule.setContent {
            RegisterScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("email-invalido")

        composeTestRule.mainClock.advanceTimeBy(500)

        composeTestRule
            .onNodeWithText("Email inválido", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun whenValidInput_enablesRegisterButton() {
        val testViewModel = FakeRegisterViewModel()

        composeTestRule.setContent {
            RegisterScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("teste@exemplo.com")

        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput("Senha123!")

        composeTestRule
            .onNodeWithTag("confirm_password_field")
            .performTextInput("Senha123!")

        composeTestRule.mainClock.advanceTimeBy(500)

        composeTestRule
            .onNodeWithTag("register_button")
            .assertIsEnabled()
    }

    @Test
    fun whenPasswordsDontMatch_showsError() {
        val testViewModel = FakeRegisterViewModel()

        composeTestRule.setContent {
            RegisterScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput("Senha123!")

        composeTestRule
            .onNodeWithTag("confirm_password_field")
            .performTextInput("Senha456!")

        composeTestRule.mainClock.advanceTimeBy(500)

        composeTestRule
            .onNodeWithText("As senhas não coincidem.", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun whenRegisterSuccess_navigatesToHome() {
        val testViewModel = FakeRegisterViewModel()
        every {
            navController.navigate(
                route = any<String>(),
                navOptions = null,
                navigatorExtras = null
            )
        } just Runs

        composeTestRule.setContent {
            RegisterScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }

        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("teste@exemplo.com")

        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput("Senha123!")

        composeTestRule
            .onNodeWithTag("confirm_password_field")
            .performTextInput("Senha123!")

        composeTestRule.mainClock.advanceTimeBy(500)

        composeTestRule
            .onNodeWithTag("register_button")
            .performClick()

        composeTestRule.mainClock.advanceTimeBy(1000)

        verify(timeout = 2000) {
            navController.navigate(
                route = "home",
                navOptions = null,
                navigatorExtras = null
            )
        }
    }
}