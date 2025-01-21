package com.guicarneirodev.goniometro

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.SelectionScreen
import com.guicarneirodev.goniometro.presentation.viewmodel.fake.FakeSelectionViewModel
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SelectionScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavController

    @Before
    fun setup() {
        navController = mockk(relaxed = true)
    }

    @Test
    fun whenScreenLoads_showsAllComponents() {
        val tools = listOf(
            Tool(
                id = "goniometer",
                nameResId = R.string.goniometer,
                descriptionResId = R.string.goniometer_desc,
                isAvailable = true,
                icon = R.drawable.goniometro
            )
        )
        val fakeViewModel = FakeSelectionViewModel(tools)

        composeTestRule.setContent {
            SelectionScreen(
                navController = navController,
                viewModel = fakeViewModel
            )
        }

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.tools)
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.goniometer)
            )
            .assertIsDisplayed()
    }

    @Test
    fun whenGoniometerSelected_navigatesToMainScreen() {
        // Given
        val tools = listOf(
            Tool(
                id = "goniometer",
                nameResId = R.string.goniometer,
                descriptionResId = R.string.goniometer_desc,
                isAvailable = true,
                icon = R.drawable.goniometro
            )
        )

        var currentDestination: String? = null
        val fakeViewModel = FakeSelectionViewModel(tools)

        composeTestRule.setContent {
            val navController = rememberNavController().apply {
                graph = createGraph(startDestination = "home") {
                    composable("home") { }
                    composable("login") { }
                    composable("selection") { }
                    composable("main") { }
                }

                addOnDestinationChangedListener { _, destination, _ ->
                    currentDestination = destination.route
                }
            }

            SelectionScreen(
                navController = navController,
                viewModel = fakeViewModel
            )
        }

        // When
        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.goniometer)
            )
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        // Then
        composeTestRule.waitForIdle()
        assertEquals("main", currentDestination)
    }

    @Test
    fun whenLanguageChanged_showsConfirmationDialog() {
        val fakeViewModel = FakeSelectionViewModel()

        composeTestRule.setContent {
            SelectionScreen(
                navController = navController,
                viewModel = fakeViewModel
            )
        }

        composeTestRule
            .onNodeWithText("English")
            .performClick()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.mudar_idioma)
            )
            .assertIsDisplayed()
    }
}