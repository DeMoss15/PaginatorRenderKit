package com.example.myapplication.data.remote.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiSource(
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("name")
    @Expose
    val name: String?
)