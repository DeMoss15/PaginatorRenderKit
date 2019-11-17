package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.api.apiModule
import com.example.myapplication.data.remote.repository.remoteRepositoryModule

val remoteDataModules = listOf(
    apiModule,
    remoteRepositoryModule,
    networkModule
)