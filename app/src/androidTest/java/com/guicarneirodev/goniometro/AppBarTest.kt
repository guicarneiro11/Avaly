package com.guicarneirodev.goniometro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.mockito.Mockito.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class AppBarTest {

    @Mock
    private lateinit var mockNavController: NavController

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testSearchFieldInput() {
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
    }

    @Test
    fun testNavigationIconClick() {

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
            .performClick()

        verify(mockNavController).popBackStack()
    }

    @Test
    fun testAddAngleDialog() {
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

        rule.onNodeWithText("Adicionar Articulação")
            .assertExists()

        rule.onNodeWithText("Nome da articulação")
            .performTextInput("Flexão de Joelho")

        rule.onNodeWithText("Valor encontrado")
            .performTextInput("90°")

        rule.onNodeWithText("Adicionar")
            .performClick()

        rule.onNodeWithText("Adicionar Paciente")
            .assertDoesNotExist()
    }

    @Test
    fun testCancelDialog() {
        rule.setContent {
            val mockNavController = rememberNavController()

            ResultsAppBar().AppBar(
                navController = mockNavController,
                addAngle = { _, _ -> },
                searchQuery = "",
                onSearchQueryChange = {}
            )
        }

        // Abre o diálogo
        rule.onNodeWithContentDescription("Add")
            .performClick()

        // Clica no botão de cancelar
        rule.onNodeWithText("Cancelar")
            .performClick()

        // Verifica se o diálogo foi fechado
        rule.onNodeWithText("Adicionar Articulação")
            .assertDoesNotExist()
    }
}