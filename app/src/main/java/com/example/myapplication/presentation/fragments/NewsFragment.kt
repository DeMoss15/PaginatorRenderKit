package com.example.myapplication.presentation.fragments

import androidx.lifecycle.Observer
import com.demoss.paginatorrenderkit.view.adapter.PaginatorAdapter
import com.demoss.paginatorrenderkit.view.adapter.PaginatorDiffItemCallbackFactory
import com.demoss.paginatorrenderkit.view.adapter.delegate.PaginatorAdapterDelegateFactory
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

    private val paginatorDelegate by lazy {

        val articleAdapterDelegate =
            PaginatorAdapterDelegateFactory.createForPaginatorItem(R.layout.item_article) {
                ArticleVH(it)
            }

        val headerAdapterDelegate =
            PaginatorAdapterDelegateFactory.createForPaginatorItem(R.layout.item_articles_header) {
                ArticlesHeaderVH(it)
            }

        val adapter = PaginatorAdapter(
            viewModel::loadNextPage,
            PaginatorDiffItemCallbackFactory.forPaginatorItem,
            articleAdapterDelegate,
            headerAdapterDelegate
        )

        PaginatorViewDelegate(viewModel::refresh, adapter, pvArticles)
    }

    override fun observeViewModel() {
        viewModel.paginatorState.observe(this, Observer { state ->
            paginatorDelegate.render(state)
        })
        viewModel.restart()
    }

    override fun stopObserveViewModel() {
        viewModel.paginatorState.removeObservers(this)
    }
}