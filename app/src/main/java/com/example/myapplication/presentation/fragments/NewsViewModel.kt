package com.example.myapplication.presentation.fragments

import com.demoss.paginatorrenderkit.Paginator
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.example.myapplication.base.mvvm.BasePaginatorViewModel
import com.example.myapplication.domain.model.Article
import com.example.myapplication.domain.usecase.GetTopHeadlinesUseCase

class NewsViewModel(private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase) :
    BasePaginatorViewModel() {

    override fun loadPage(page: Int) {
        getTopHeadlinesUseCase.execute(
            onSuccess = { newPageData ->

                paginatorStore.proceed(
                    Paginator.Action.NewPage(
                        pageNumber = page,
                        items = mapArticles(newPageData),
                        isLastPage = newPageData.isEmpty()
                    )
                )
                if (page == 1) addHeaderItem()

            },
            onError =  { error -> paginatorStore.proceed(Paginator.Action.PageError(error)) },
            params = GetTopHeadlinesUseCase.Params(page))
    }

    override fun cancelLoading() {
        getTopHeadlinesUseCase.clear()
    }

    private fun addHeaderItem() {
        paginatorStore.proceed(Paginator.Action.EditCurrentStateData { data ->
            val newData = data.toMutableList()
            newData.add(0, createHeaderPaginatorItem())
            newData
        })
    }

    private fun mapArticles(data: List<Article>): List<PaginatorItem<Article>> = data.map { article ->
        PaginatorItem(article) { otherArticle -> article.url == otherArticle.url }
    }

    private fun createHeaderPaginatorItem(): PaginatorItem<ArticlesHeader> =
        PaginatorItem(ArticlesHeader) { true }
}