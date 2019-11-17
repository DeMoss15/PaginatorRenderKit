package com.example.myapplication.domain.model

import com.example.myapplication.domain.model.Source

data class Article(
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val image: String,
    val publishedAt: String
)