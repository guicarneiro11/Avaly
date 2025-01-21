package com.guicarneirodev.goniometro

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
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
import junit.framework.TestCase.assertEquals
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
    fun whenToolCardClicked_navigatesToTool() {
        var navigatedDestination: String? = null
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
            val fakeNavController = rememberNavController().apply {
                navigatedDestination = null
                addOnDestinationChangedListener { _, destination, _ ->
                    navigatedDestination = destination.route
                }
                graph = createGraph(startDestination = "home") {
                    composable("home") { }
                    composable("main") { }
                }
            }

            SelectionScreen(
                navController = fakeNavController,
                viewModel = fakeViewModel
            )
        }

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.goniometer)
            )
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals("main", navigatedDestination)
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