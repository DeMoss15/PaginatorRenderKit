package com.example.myapplication.data.remote.api

import com.example.myapplication.Constants
import com.example.myapplication.data.remote.api.model.ResponseObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsApi {

    @GET("/v2/top-headlines")
    fun getTopNewsAsync(@QueryMap options: Map<String, String?>): Single<ResponseObject>
}

fun getBaseRequest(): Map<String, String> = mapOf(
    "language" to "en",
    NewsApiConstants.API_KEY to Constants.API_KEY,
    NewsApiConstants.PAGE_SIZE to Constants.PAGE_SIZE.toString()
)