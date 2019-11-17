package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.usecase.base.RxUseCaseSingle
import com.example.myapplication.data.repository.TopHeadlinesRepository
import com.example.myapplication.domain.model.Article
import io.reactivex.Single

class GetTopHeadlinesUseCase(private val topHeadlinesRepository: TopHeadlinesRepository) :
    RxUseCaseSingle<List<Article>, GetTopHeadlinesUseCase.Params>() {

    override fun buildUseCaseObservable(params: Params): Single<List<Article>> = params.let {
        topHeadlinesRepository.getTopHeadlines(it.page, it.query, it.sources, it.category, it.country)
    }

    class Params(
        var page: Int,
        var query: String? = null,
        var sources: String? = null,
        var category: String? = null,
        var country: String? = null
    )
}