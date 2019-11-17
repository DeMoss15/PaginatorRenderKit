package com.example.myapplication.presentation.fragments

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val fragmentsModule = module {
    viewModel { NewsViewModel(get()) }
}