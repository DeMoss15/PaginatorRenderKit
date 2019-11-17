package com.example.myapplication.data.remote

import com.example.myapplication.Constants
import com.example.myapplication.Constants.CONNECTION_TIMEOUT
import com.example.myapplication.Constants.READ_TIMEOUT
import com.example.myapplication.Constants.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        // Retrofit
        Retrofit.Builder().apply {
            baseUrl(Constants.BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(get())
        }.build()
    }
    single {
        // OkHttp
        OkHttpClient.Builder().apply {
            retryOnConnectionFailure(true)
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }
}