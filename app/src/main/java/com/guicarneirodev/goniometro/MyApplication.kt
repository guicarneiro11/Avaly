package com.guicarneirodev.goniometro

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.guicarneirodev.goniometro.data.local.database.AppDatabase
import com.guicarneirodev.goniometro.data.local.mapper.UserMapper
import com.guicarneirodev.goniometro.data.local.mapper.UserPreferencesMapper
import com.guicarneirodev.goniometro.data.repository.ResultsRepositoryImpl
import com.guicarneirodev.goniometro.data.repository.ToolsRepositoryImpl
import com.guicarneirodev.goniometro.data.repository.UserPreferencesRepositoryImpl
import com.guicarneirodev.goniometro.domain.repository.LoginPreferencesRepository
import com.guicarneirodev.goniometro.domain.repository.SharedPreferencesRepository
import com.guicarneirodev.goniometro.domain.repository.ToolsRepository
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import com.guicarneirodev.goniometro.domain.usecase.GetAvailableToolsUseCase
import com.guicarneirodev.goniometro.domain.usecase.GetUserPreferencesUseCase
import com.guicarneirodev.goniometro.domain.usecase.LogoutUseCase
import com.guicarneirodev.goniometro.domain.usecase.SaveUserPreferencesUseCase
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionViewModel
import com.guicarneirodev.goniometro.utils.LocaleHelper
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
            modules(
                listOf(
                    appModule,
                    selectionModule,
                    databaseModule,
                    mapperModule
                )
            )
        }
    }
}

val mapperModule = module {
    single { UserMapper() }
    single { UserPreferencesMapper() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "avaly-database"
        ).build()
    }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().preferencesDao() }
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
    single { LocaleHelper(get()) }
    single<LoginPreferencesRepository> {
        SharedPreferencesRepository(
            get<Context>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        )
    }
    single<ToolsRepository> { ToolsRepositoryImpl() }
    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(get(), get(), get(), get())
    }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { get<Context>().preferencesDataStoreFile("user_preferences") }
        )
    }

    factory { GetAvailableToolsUseCase(get()) }
    factory { GetUserPreferencesUseCase(get()) }
    factory { SaveUserPreferencesUseCase(get()) }
    factory { LogoutUseCase(get(), get()) }

    viewModel { SelectionViewModel(get(), get(), get(), get()) }
}