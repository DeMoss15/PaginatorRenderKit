package com.example.myapplication.data.repository

import com.example.myapplication.data.repository.TopHeadlinesDataSource
import com.example.myapplication.data.repository.TopHeadlinesRepository
import org.koin.dsl.module.module

val repositoryDataModule = module {
    single { TopHeadlinesDataSource(get()) as TopHeadlinesRepository }
}