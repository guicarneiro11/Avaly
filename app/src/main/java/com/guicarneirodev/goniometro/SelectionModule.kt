package com.guicarneirodev.goniometro

import android.content.Context
import androidx.datastore.core.DataStore
import com.guicarneirodev.goniometro.data.repository.ToolsRepositoryImpl
import com.guicarneirodev.goniometro.data.repository.UserPreferencesRepositoryImpl
import com.guicarneirodev.goniometro.domain.repository.ToolsRepository
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import com.guicarneirodev.goniometro.domain.usecase.GetAvailableToolsUseCase
import com.guicarneirodev.goniometro.domain.usecase.GetUserPreferencesUseCase
import com.guicarneirodev.goniometro.domain.usecase.SaveUserPreferencesUseCase
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.util.prefs.Preferences

val selectionModule = module {
    // Repositories
    single<ToolsRepository> { ToolsRepositoryImpl() }
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

    // Use Cases
    factory { GetAvailableToolsUseCase(get()) }
    factory { GetUserPreferencesUseCase(get()) }
    factory { SaveUserPreferencesUseCase(get()) }

    // ViewModel
    viewModel { SelectionViewModel(get(), get(), get()) }
}