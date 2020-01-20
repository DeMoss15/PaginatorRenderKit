package com.example.myapplication.presentation.fragments

import androidx.lifecycle.Observer
import com.demoss.paginatorrenderkit.view.adapter.PaginatorAdapter
import com.demoss.paginatorrenderkit.view.adapter.PaginatorDiffItemCallbackFabric
import com.demoss.paginatorrenderkit.view.adapter.delegate.PaginatorAdapterDelegateFabric
import com.demoss.paginatorrenderkit.view.delegate.PaginatorViewDelegate
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.domain.model.Article
import kotlinx.android.synthetic.main.fragment_news_empty_progress.*
import org.koin.android.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment<NewsViewModel>() {

    companion object {
        const val TAG = "com.example.myapplication.presentation.fragments.NewsFragment"
    }

    override val layoutResourceId: Int = R.layout.fragment_news_empty_progress
    override val viewModel by viewModel<NewsViewModel>()

    private val paginatorDelegate by lazy {
        PaginatorViewDelegate<AbsPaginatorItem<*>>(
            viewModel::refresh,
            PaginatorAdapter(
                viewModel::loadNextPage,
                PaginatorDiffItemCallbackFabric.create<AbsPaginatorItem<*>>(
                    { oldItem, newItem -> oldItem.areItemsTheSame(newItem) },
                    { oldItem, newItem -> oldItem.getChangePayload(newItem) }
                ),
                ///////////////////////////////////////////////////////////////////////////
                // both delegates are for AbsPaginatorItem
                ///////////////////////////////////////////////////////////////////////////
                PaginatorAdapterDelegateFabric.create(
                    R.layout.item_article,
                    { item -> item.isForViewType(Article::class.java) }
                ) { ArticleVH(it) },
                PaginatorAdapterDelegateFabric.create(
                    R.layout.item_articles_header,
                    { item -> item.isForViewType(ArticlesHeader::class.java) }
                ) { ArticlesHeaderVH(it) }
            ),
            pvArticles
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