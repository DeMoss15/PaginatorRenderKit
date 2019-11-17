package com.example.myapplication.data.remote.api

import org.koin.dsl.module.module
import retrofit2.Retrofit

val apiModule = module {

    single { getApi(get()) }
}

private fun getApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
