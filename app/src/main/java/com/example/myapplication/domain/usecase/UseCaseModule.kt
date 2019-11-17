package com.example.myapplication.domain.usecase

import org.koin.dsl.module.module

val useCaseModule = module {
    factory { GetTopHeadlinesUseCase(get()) }
}