package com.example.myapplication.presentation.fragments

import androidx.lifecycle.Observer
import com.demoss.paginatorrenderkit.view.adapter.delegate.mutableAdapterDelegate
import com.demoss.paginatorrenderkit.view.delegate.PaginatorViewDelegate
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_news_empty_progress.*
import org.koin.android.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment<NewsViewModel>() {

    companion object {
        const val TAG = "com.example.myapplication.presentation.fragments.NewsFragment"
    }

    override val layoutResourceId: Int = R.layout.fragment_news_empty_progress
    override val viewModel by viewModel<NewsViewModel>()

    private val paginatorDelegate: PaginatorViewDelegate by lazy {
        PaginatorViewDelegate(
            viewModel::refresh,
            viewModel::loadNextPage,
            pvArticles,
            mutableAdapterDelegate(R.layout.item_article) { ArticleVH(it) }
        )
    }

    override fun observeViewModel() {
        viewModel.paginatorState.observe(this, Observer {
            paginatorDelegate.render(it)
        })
        viewModel.restart()
    }

    override fun stopObserveViewModel() {
        viewModel.paginatorState.removeObservers(this)
    }
}