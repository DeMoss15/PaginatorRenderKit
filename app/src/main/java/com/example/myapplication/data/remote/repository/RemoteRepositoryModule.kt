package com.example.myapplication.data.remote.repository

import com.example.myapplication.data.remote.repository.TopHeadlinesRemoteDataSource
import com.example.myapplication.data.remote.repository.TopHeadlinesRemoteRepository
import org.koin.dsl.module.module

val remoteRepositoryModule = module {
    single { TopHeadlinesRemoteDataSource(get()) as TopHeadlinesRemoteRepository }
}