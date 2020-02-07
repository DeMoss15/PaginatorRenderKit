package com.example.myapplication.presentation.fragments

import com.demoss.paginatorrenderkit.Paginator
import com.example.myapplication.base.mvvm.BasePaginatorViewModel
import com.example.myapplication.domain.model.Article
import com.example.myapplication.domain.usecase.GetTopHeadlinesUseCase
import com.example.myapplication.domain.usecase.defaultobserver.DefaultSingleObserver

class NewsViewModel(private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase) :
    BasePaginatorViewModel() {

    override fun loadPage(page: Int) {
        getTopHeadlinesUseCase.execute(object : DefaultSingleObserver<List<Article>>() {
            override fun onSuccess(t: List<Article>) {
                paginator.proceed(
                    Paginator.Action.NewPage(page, t, t.isEmpty())
                )
                if (page == 1) {
                    paginator.proceed(Paginator.Action.EditCurrentStateData { data ->
                        val newData = data.toMutableList()
                        newData.add(0, ArticlesHeader)
                        newData
                    })
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }, GetTopHeadlinesUseCase.Params(page))
    }

    override fun cancelLoading() {
        getTopHeadlinesUseCase.clear()
    }
}