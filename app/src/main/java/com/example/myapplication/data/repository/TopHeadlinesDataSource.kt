package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.repository.TopHeadlinesRemoteRepository
import com.example.myapplication.domain.model.Article
import io.reactivex.Single

class TopHeadlinesDataSource(val remoteRepository: TopHeadlinesRemoteRepository) :
    TopHeadlinesRepository {

    override fun getTopHeadlines(
        page: Int,
        query: String?,
        sources: String?,
        category: String?,
        country: String?
    ): Single<List<Article>>  =
        remoteRepository.getTopHeadlines(page, query, sources, category, country)
}