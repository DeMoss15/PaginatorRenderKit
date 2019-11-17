package com.example.myapplication.data.remote.repository

import com.example.myapplication.domain.model.Article
import io.reactivex.Single

interface TopHeadlinesRemoteRepository {
    fun getTopHeadlines(
        page: Int,
        query: String? = null,
        sources: String? = null,
        category: String? = null,
        country: String? = null
    ): Single<List<Article>>
}