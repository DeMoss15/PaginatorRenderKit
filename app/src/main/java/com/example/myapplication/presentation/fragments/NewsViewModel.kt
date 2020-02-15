package com.example.myapplication.presentation.fragments

import com.demoss.paginatorrenderkit.Paginator
import com.example.myapplication.base.mvvm.BasePaginatorViewModel
import com.example.myapplication.domain.usecase.GetTopHeadlinesUseCase

class NewsViewModel(private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase) :
    BasePaginatorViewModel() {

    override fun loadPage(page: Int) {
        getTopHeadlinesUseCase.execute(
            onSuccess = { newPageData ->
                paginatorStore.proceed(
                    Paginator.Action.NewPage(page, newPageData, newPageData.isEmpty())
                )
                if (page == 1) {
                    paginatorStore.proceed(Paginator.Action.EditCurrentStateData { data ->
                        val newData = data.toMutableList()
                        newData.add(0, ArticlesHeader)
                        newData
                    })
                }
            },
            onError =  { error -> paginatorStore.proceed(Paginator.Action.PageError(error)) },
            params = GetTopHeadlinesUseCase.Params(page))
    }

    override fun cancelLoading() {
        getTopHeadlinesUseCase.clear()
    }
}