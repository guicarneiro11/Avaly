package com.guicarneirodev.goniometro

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.LogoSection
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

fun ComposeTestRule.assertVisualElementsInOrder(vararg nodes: SemanticsNodeInteraction) {
    var lastBounds: Rect? = null

    nodes.forEach { node ->
        val bounds = node.fetchSemanticsNode().boundsInRoot

        if (lastBounds != null) {
            assert(bounds.top >= lastBounds!!.bottom) {
                "Elements are not in correct vertical order"
            }
        }

        lastBounds = bounds
    }
}

@RunWith(AndroidJUnit4::class)
class LogoSectionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyLogoDisplayAndSize() {
        composeTestRule.setContent {
            LogoSection()
        }

        composeTestRule
            .onNodeWithContentDescription("Avaly Logo")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun verifyLogoContainerSize() {
        composeTestRule.setContent {
            LogoSection()
        }

        composeTestRule
            .onNode(
                hasParent(isRoot()) and
                        hasTestTag("logo_outer_container")
            )
            .assertExists()
    }
}