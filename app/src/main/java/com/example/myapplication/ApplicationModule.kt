package com.example.myapplication

import com.example.myapplication.data.dataModules
import com.example.myapplication.domain.domainModules
import com.example.myapplication.presentation.presentationModules
import org.koin.dsl.module.Module

val applicationModule: List<Module> = presentationModules +
        domainModules +
        dataModules