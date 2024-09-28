package com.guicarneirodev.goniometro

import com.guicarneirodev.goniometro.data.repository.ResultsRepositoryImpl
import com.guicarneirodev.goniometro.presentation.viewmodel.ResultsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

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