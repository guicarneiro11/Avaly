package com.guicarneirodev.goniometro

import android.content.Context
import android.content.res.Configuration
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.domain.usecase.GetAvailableToolsUseCase
import com.guicarneirodev.goniometro.domain.usecase.GetUserPreferencesUseCase
import com.guicarneirodev.goniometro.domain.usecase.SaveUserPreferencesUseCase
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionViewModel
import com.guicarneirodev.goniometro.utils.LocaleHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SelectionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SelectionViewModel
    private lateinit var localeHelper: LocaleHelper
    private lateinit var getAvailableToolsUseCase: GetAvailableToolsUseCase
    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase
    private lateinit var saveUserPreferencesUseCase: SaveUserPreferencesUseCase
    private lateinit var context: Context

    @Before
    fun setup() {
        context = mockk<Context>(relaxed = true) {
            every { resources } returns mockk {
                every { configuration } returns Configuration()
            }
            every { createConfigurationContext(any()) } returns mockk()
        }

        localeHelper = LocaleHelper(context)

        getAvailableToolsUseCase = mockk(relaxed = true)
        getUserPreferencesUseCase = mockk(relaxed = true)
        saveUserPreferencesUseCase = mockk(relaxed = true)

        coEvery { getAvailableToolsUseCase() } returns listOf(
            Tool(
                id = "goniometer",
                nameResId = R.string.goniometer,
                descriptionResId = R.string.goniometer_desc,
                isAvailable = true,
                icon = R.drawable.goniometro
            )
        )
        coEvery { getUserPreferencesUseCase() } returns UserPreferences()
        coEvery { saveUserPreferencesUseCase(any()) } just runs

        viewModel = SelectionViewModel(
            localeHelper,
            getAvailableToolsUseCase,
            getUserPreferencesUseCase,
            saveUserPreferencesUseCase
        )
    }

    @Test
    fun `when initialized, loads tools and preferences`() = runTest {
        val expectedTools = listOf(
            Tool(
                id = "goniometer",
                nameResId = R.string.goniometer,
                descriptionResId = R.string.goniometer_desc,
                isAvailable = true,
                icon = R.drawable.goniometro
            )
        )
        val expectedPreferences = UserPreferences()

        viewModel = SelectionViewModel(
            localeHelper,
            getAvailableToolsUseCase,
            getUserPreferencesUseCase,
            saveUserPreferencesUseCase
        )

        assertEquals(expectedTools, viewModel.uiState.value.tools)
        assertEquals(expectedPreferences, viewModel.uiState.value.userPreferences)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `when updating language, saves preferences and updates locale`() = runTest {
        val newLanguage = Language.ENGLISH

        viewModel.updateLanguage(newLanguage)

        coVerify {
            saveUserPreferencesUseCase(match {
                it.language == newLanguage
            })
        }

        assertEquals(newLanguage, viewModel.uiState.value.userPreferences.language)
    }

    @Test
    fun `when updating user type, saves preferences`() = runTest {
        val newUserType = UserType.PROFESSIONAL

        viewModel.updateUserType(newUserType)

        coVerify { saveUserPreferencesUseCase(match { it.userType == newUserType }) }
        assertEquals(newUserType, viewModel.uiState.value.userPreferences.userType)
    }

    @Test
    fun `when loading fails, shows error message`() = runTest {
        coEvery { getAvailableToolsUseCase() } throws Exception("Network error")

        viewModel = SelectionViewModel(
            localeHelper,
            getAvailableToolsUseCase,
            getUserPreferencesUseCase,
            saveUserPreferencesUseCase
        )

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Network error", viewModel.uiState.value.errorMessage)
    }
}