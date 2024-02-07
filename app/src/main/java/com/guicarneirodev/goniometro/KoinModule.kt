package com.guicarneirodev.goniometro

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ValidViewModel() }
}