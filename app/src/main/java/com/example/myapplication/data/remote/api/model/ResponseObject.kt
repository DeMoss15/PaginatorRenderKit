package com.example.myapplication.data.remote.api.model

import com.example.myapplication.data.remote.api.model.ApiArticle
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseObject(
    @SerializedName("status")
    @Expose
    val status: String?,
    @SerializedName("totalResults")
    @Expose
    val totalResults: Int?,
    @SerializedName("articles")
    @Expose
    val articles: List<ApiArticle>?
)