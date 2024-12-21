package com.guicarneirodev.goniometro

import android.app.Application
import com.guicarneirodev.goniometro.data.repository.ResultsRepositoryImpl
import com.guicarneirodev.goniometro.data.repository.ToolsRepositoryImpl
import com.guicarneirodev.goniometro.data.repository.UserPreferencesRepositoryImpl
import com.guicarneirodev.goniometro.domain.repository.ToolsRepository
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import com.guicarneirodev.goniometro.domain.usecase.GetAvailableToolsUseCase
import com.guicarneirodev.goniometro.domain.usecase.GetUserPreferencesUseCase
import com.guicarneirodev.goniometro.domain.usecase.SaveUserPreferencesUseCase
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(
                appModule,
                selectionModule)
            )
        }
    }
}

val appModule = module {
    factory { (userId: String, patientId: String) ->
        ResultsRepositoryImpl(userId, patientId)
    }

    viewModel { (userId: String, patientId: String) ->
        ResultsScreenViewModel(
            repository = get<ResultsRepositoryImpl> { parametersOf(userId, patientId) }
        )
    }
}

val selectionModule = module {
    single<ToolsRepository> { ToolsRepositoryImpl() }
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

    factory { GetAvailableToolsUseCase(get()) }
    factory { GetUserPreferencesUseCase(get()) }
    factory { SaveUserPreferencesUseCase(get()) }

    viewModel { SelectionViewModel(get(), get(), get()) }
}