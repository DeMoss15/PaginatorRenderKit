package com.example.myapplication.data.remote.repository

import com.example.myapplication.data.remote.RemoteDomainMapper
import com.example.myapplication.data.remote.api.NewsApi
import com.example.myapplication.data.remote.api.requests.NewsApiTopNewsRequestBuilder
import com.example.myapplication.domain.model.Article
import io.reactivex.Single

class TopHeadlinesRemoteDataSource(private val newsApi: NewsApi) : TopHeadlinesRemoteRepository {

    override fun getTopHeadlines(
        page: Int,
        query: String?,
        sources: String?,
        category: String?,
        country: String?
    ): Single<List<Article>> = newsApi.getTopNewsAsync(NewsApiTopNewsRequestBuilder().apply {
        this.page = page
        this.query = query
        this.sources = sources
        this.category = category
        this.country = country
    }.build()).map {
        RemoteDomainMapper.toDomain(it.articles ?: listOf())
    }
}