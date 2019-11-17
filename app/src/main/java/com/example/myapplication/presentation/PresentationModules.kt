package com.example.myapplication.presentation

import com.example.myapplication.presentation.fragments.fragmentsModule
import org.koin.dsl.module.Module

val presentationModules = listOf<Module>(fragmentsModule)