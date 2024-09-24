package com.guicarneirodev.goniometro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.mockito.Mockito.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockNavController: NavController
    private lateinit var mockAddAngle: (String, String) -> Unit
    private lateinit var mockOnSearchQueryChange: (String) -> Unit

    @Before
    fun setup() {
        mockNavController = mock(NavController::class.java)
        mockAddAngle = mock()
        mockOnSearchQueryChange = mock()
    }

    @Test
    fun testSearchFieldInputAndFocus() {
        var searchQuery by mutableStateOf("")
        composeTestRule.setContent {
            ResultsAppBar(
                navController = mockNavController,
                onAddAngle = mockAddAngle,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }

        val testText = "Buscar Teste"
        composeTestRule.onNodeWithTag("searchField")
            .performTextInput(testText)

        composeTestRule.onNodeWithTag("searchField")
            .assertTextEquals(testText)
            .assertIsFocused()

        composeTestRule.onNodeWithTag("searchField")
            .performTextClearance()
    }

    @Test
    fun testNavigationIconClickAndAppearance() {
        composeTestRule.setContent {
            ResultsAppBar(
                navController = mockNavController,
                onAddAngle = mockAddAngle,
                searchQuery = "",
                onSearchQueryChange = mockOnSearchQueryChange
            )
        }

        composeTestRule.onNodeWithContentDescription("Voltar Tela")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(mockNavController).popBackStack()
    }

    @Test
    fun testAddAngleDialogInteractions() {
        composeTestRule.setContent {
            ResultsAppBar(
                navController = mockNavController,
                onAddAngle = mockAddAngle,
                searchQuery = "",
                onSearchQueryChange = mockOnSearchQueryChange
            )
        }

        composeTestRule.onNodeWithContentDescription("Add")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText("Adicionar Articulação")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Nome da articulação")
            .performTextInput("Flexão de Joelho")

        composeTestRule.onNodeWithText("Nome da articulação")
            .assertTextContains("Flexão de Joelho")

        composeTestRule.onNodeWithText("Valor encontrado")
            .performTextInput("90")

        composeTestRule.onNodeWithText("Valor encontrado")
            .assertTextContains("90°")

        composeTestRule.onNodeWithText("Adicionar")
            .assertIsEnabled()
            .performClick()

        verify(mockAddAngle).invoke("Flexão de Joelho", "90°")

        composeTestRule.onNodeWithText("Adicionar Articulação")
            .assertDoesNotExist()
    }

    @Test
    fun testCancelDialogAndEmptyFields() {
        composeTestRule.setContent {
            ResultsAppBar(
                navController = mockNavController,
                onAddAngle = mockAddAngle,
                searchQuery = "",
                onSearchQueryChange = mockOnSearchQueryChange
            )
        }

        composeTestRule.onNodeWithContentDescription("Add")
            .performClick()

        composeTestRule.onNodeWithText("Nome da articulação")
            .assertTextContains("")

        composeTestRule.onNodeWithText("Valor encontrado")
            .assertTextContains("")

        composeTestRule.onNodeWithText("Cancelar")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText("Adicionar Articulação")
            .assertDoesNotExist()
    }

    @Test
    fun testAppBarAccessibility() {
        composeTestRule.setContent {
            ResultsAppBar(
                navController = mockNavController,
                onAddAngle = mockAddAngle,
                searchQuery = "",
                onSearchQueryChange = mockOnSearchQueryChange
            )
        }

        composeTestRule.onNodeWithContentDescription("Voltar Tela").assertExists()
        composeTestRule.onNodeWithContentDescription("Add").assertExists()

        composeTestRule.onNodeWithTag("searchField")
            .assert(hasSetTextAction())
            .assert(hasText("Buscar articulação"))
            .assert(hasImeAction(ImeAction.Done))
    }

    @Test
    fun testSearchFieldBehavior() {
        var searchQuery by mutableStateOf("")
        composeTestRule.setContent {
            ResultsAppBar(
                navController = mockNavController,
                onAddAngle = mockAddAngle,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }

        composeTestRule.onNodeWithTag("searchField")
            .performTextInput("Test")

        composeTestRule.onNodeWithTag("searchField")
            .assertTextEquals("Test")

        composeTestRule.onNodeWithTag("searchField")
            .performImeAction()

        composeTestRule.onNodeWithTag("searchField")
            .assertIsNotFocused()
    }
}