package com.example.myapplication.data.local

import com.example.myapplication.data.local.dbModule
import com.example.myapplication.data.local.repository.localRepositoryModule

val localDataModules = listOf(
    dbModule,
    localRepositoryModule
)