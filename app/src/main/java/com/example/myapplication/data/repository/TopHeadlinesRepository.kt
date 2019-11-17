package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.Article
import io.reactivex.Single

interface TopHeadlinesRepository {
    fun getTopHeadlines(
        page: Int,
        query: String? = null,
        sources: String? = null,
        category: String? = null,
        country: String? = null
    ): Single<List<Article>>
}