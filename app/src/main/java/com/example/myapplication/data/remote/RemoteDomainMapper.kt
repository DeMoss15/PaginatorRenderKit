package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.api.model.ApiArticle
import com.example.myapplication.data.remote.api.model.ApiSource
import com.example.myapplication.domain.model.Article
import com.example.myapplication.domain.model.Source
import com.example.myapplication.util.Empty

object RemoteDomainMapper {

    fun toDomain(remoteArticles: List<ApiArticle>): List<Article> =
        remoteArticles.map { toDomain(it) }

    fun toDomain(remoteArticle: ApiArticle?): Article = Article(
        toDomain(remoteArticle?.source),
        remoteArticle?.author ?: Empty.EMPTY_STRING,
        remoteArticle?.title ?: Empty.EMPTY_STRING,
        remoteArticle?.description ?: Empty.EMPTY_STRING,
        remoteArticle?.url ?: Empty.EMPTY_STRING,
        remoteArticle?.urlToImage ?: Empty.EMPTY_STRING,
        remoteArticle?.publishedAt ?: Empty.EMPTY_STRING
    )

    fun toDomain(remoteSource: ApiSource?): Source = Source(
        remoteSource?.id ?: Empty.EMPTY_STRING,
        remoteSource?.name ?: Empty.EMPTY_STRING
    )
}