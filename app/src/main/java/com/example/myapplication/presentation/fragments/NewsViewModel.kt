package com.example.myapplication.presentation.fragments

import android.util.Log
import com.demoss.paginatorrenderkit.Paginator
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.example.myapplication.base.mvvm.BasePaginatorViewModel
import com.example.myapplication.domain.model.Article
import com.example.myapplication.domain.usecase.GetTopHeadlinesUseCase
import com.example.myapplication.domain.usecase.defaultobserver.DefaultSingleObserver

class NewsViewModel(private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase) :
    BasePaginatorViewModel() {

    override fun loadPage(page: Int) {
        Log.d("DEB_TAG", "\nNewsViewModel loadPage\nLOG:\n"
            + "$page")
        getTopHeadlinesUseCase.execute(object : DefaultSingleObserver<List<Article>>() {
            override fun onSuccess(t: List<Article>) {
                Log.d("DEB_TAG", "\nNewsViewModel onSuccess\nLOG:\n")
                paginator.proceed(
                    Paginator.Action.NewPage(
                        page,
                        t.map { article ->
                            PaginatorItem(
                                article
                            ) { article.url == it.url }
                        },
                        t.isEmpty()
                    )
                )
            }

            override fun onError(e: Throwable) {
                Log.d("DEB_TAG", "\nNewsViewModel onError\nLOG:\n")
                super.onError(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }, GetTopHeadlinesUseCase.Params(page))
    }

    override fun cancelLoading() {
        getTopHeadlinesUseCase.clear()
    }
}