package com.guicarneirodev.goniometro.ui.theme

import com.guicarneirodev.goniometro.ValidViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ValidViewModel() }
}
