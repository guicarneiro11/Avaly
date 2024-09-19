package com.guicarneirodev.goniometro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.mockito.Mockito.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class AppBarTest {

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testSearchFieldInputAndFocus() {
        rule.setContent {
            val mockNavController = rememberNavController()
            var searchQuery by remember { mutableStateOf("") }

            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }

        val testText = "Buscar Teste"
        rule.onNodeWithTag("searchField")
            .performTextInput(testText)

        rule.onNodeWithTag("searchField")
            .assertTextEquals(testText)
            .assertIsFocused()

        rule.onNodeWithTag("searchField")
            .assertTextEquals(testText)

        rule.onNodeWithTag("searchField")
            .performTextClearance()
    }

    @Test
    fun testNavigationIconClickAndAppearance() {
        val mockNavController = mock(NavController::class.java)
        `when`(mockNavController.popBackStack()).thenReturn(true)

        rule.setContent {
            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = "",
                onSearchQueryChange = {}
            )
        }

        rule.onNodeWithContentDescription("Voltar Tela")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(mockNavController).popBackStack()
    }

    @Test
    fun testAddAngleDialogInteractions() {
        rule.setContent {
            val mockNavController = rememberNavController()

            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = "",
                onSearchQueryChange = {}
            )
        }

        rule.onNodeWithContentDescription("Add")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        rule.onNodeWithText("Adicionar Articulação")
            .assertIsDisplayed()

        rule.onNodeWithText("Nome da articulação")
            .performTextInput("Flexão de Joelho")

        rule.onNodeWithText("Nome da articulação")
            .assertTextContains("Flexão de Joelho")

        rule.onNodeWithText("Valor encontrado")
            .performTextInput("90°")

        rule.onNodeWithText("Valor encontrado")
            .assertTextContains("90°")

        rule.onNodeWithText("Adicionar")
            .assertIsEnabled()
            .performClick()

        rule.onNodeWithText("Adicionar Paciente")
            .assertDoesNotExist()
    }

    @Test
    fun testCancelDialogAndEmptyFields() {
        rule.setContent {
            val mockNavController = rememberNavController()

            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = "",
                onSearchQueryChange = {}
            )
        }

        rule.onNodeWithContentDescription("Add")
            .performClick()

        rule.onNodeWithText("Nome da articulação")
            .assertTextContains("")

        rule.onNodeWithText("Valor encontrado")
            .assertTextContains("")

        rule.onNodeWithText("Cancelar")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        rule.onNodeWithText("Adicionar Articulação")
            .assertDoesNotExist()
    }

    @Test
    fun testAppBarAccessibility() {
        rule.setContent {
            val mockNavController = rememberNavController()

            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = "",
                onSearchQueryChange = {}
            )
        }

        rule.onNodeWithContentDescription("Voltar Tela").assertExists()
        rule.onNodeWithContentDescription("Add").assertExists()

        rule.onNodeWithTag("searchField")
            .assert(hasSetTextAction())
            .assert(hasText("Buscar articulação"))
            .assert(hasImeAction(ImeAction.Done))

        rule.onNodeWithTag("searchField")
            .assert(hasText(""))

        rule.onNodeWithTag("searchField")
            .assert(hasText("Buscar articulação"))
    }

    @Test
    fun testSearchFieldBehavior () {
        rule.setContent {
            val mockNavController = rememberNavController()
            var searchQuery by remember { mutableStateOf("") }

            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }

        rule.onNodeWithTag("searchField")
            .performTextInput("Test")

        rule.onNodeWithTag("searchField")
            .assertTextEquals("Test")

        rule.onNodeWithTag("searchField")
            .performImeAction()

        rule.onNodeWithTag("searchField")
            .assertIsNotFocused()
    }
}