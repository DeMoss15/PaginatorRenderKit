package com.example.myapplication.presentation.fragments

import androidx.lifecycle.Observer
import com.demoss.paginatorrenderkit.Paginator
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
        PaginatorViewDelegate(
            viewModel::refresh,
            PaginatorAdapter(
                viewModel::loadNextPage,
                PaginatorDiffItemCallbackFabric.create<AbsPaginatorItem<Article>>(
                    { oldItem, newItem -> oldItem.areItemsTheSame(newItem) },
                    { oldItem, newItem -> oldItem.getChangePayload(newItem) }
                ),
                PaginatorAdapterDelegateFabric.create<AbsPaginatorItem<Article>>(
                    R.layout.item_article,
                    { item -> item.isForViewType(Article::class.java) }
                ) { ArticleVH(it) }
            ),
            pvArticles
        )
    }

    override fun observeViewModel() {
        viewModel.paginatorState.observe(this, Observer {
            paginatorDelegate.render(it as Paginator.State<Any>)
        })
        viewModel.restart()
    }

    override fun stopObserveViewModel() {
        viewModel.paginatorState.removeObservers(this)
    }
}