package com.example.myapplication.domain

import com.example.myapplication.domain.model.modelModule
import com.example.myapplication.domain.usecase.useCaseModule

val domainModules = listOf(
    useCaseModule,
    modelModule
)