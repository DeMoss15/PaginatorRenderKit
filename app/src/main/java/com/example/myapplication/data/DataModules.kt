package com.example.myapplication.data

import com.example.myapplication.data.local.localDataModules
import com.example.myapplication.data.remote.remoteDataModules
import com.example.myapplication.data.repository.repositoryDataModule

val dataModules = localDataModules +
        remoteDataModules +
        repositoryDataModule