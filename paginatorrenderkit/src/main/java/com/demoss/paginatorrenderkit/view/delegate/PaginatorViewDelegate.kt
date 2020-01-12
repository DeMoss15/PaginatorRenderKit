package com.demoss.paginatorrenderkit.view.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demoss.paginatorrenderkit.Paginator
import com.demoss.paginatorrenderkit.view.adapter.PaginatorAdapter
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class PaginatorViewDelegate(
    private val refreshCallback: (() -> Unit)? = null,
    nextPageCallback: (() -> Unit)? = null,
    private val recyclerView: RecyclerView,
    private val swipeToRefresh: SwipeRefreshLayout,
    private val emptyView: AbsPaginatorEmptyView,
    private val fullscreenProgressView: View,
    vararg delegate: AdapterDelegate<MutableList<PaginatorItem<*>>>
) {

    constructor(
        refreshCallback: (() -> Unit)? = null,
        nextPageCallback: (() -> Unit)? = null,
        paginatorView: AbsPaginatorView,
        vararg delegate: AdapterDelegate<MutableList<PaginatorItem<*>>>
    ) : this(
        refreshCallback,
        nextPageCallback,
        paginatorView.getRecyclerView(),
        paginatorView.getSwipeRefreshLayout(),
        paginatorView.getEmptyView(),
        paginatorView.getFullScreenProgressView(),
        *delegate
    )

    private var adapter: PaginatorAdapter = PaginatorAdapter(nextPageCallback, *delegate)

    init {
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        recyclerView.adapter = adapter
    }

    fun render(state: Paginator.State<PaginatorItem<*>>) {
        recyclerView.post {
            when (state) {
                is Paginator.State.Empty -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter.fullData = true
                    adapter.update(emptyList(), false)
                    emptyView.showEmptyData()
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.EmptyProgress -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(true)
                    adapter.fullData = false
                    adapter.update(emptyList(), false)
                    emptyView.visible(false)
                    swipeToRefresh.visible(false)
                }
                is Paginator.State.EmptyError -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter.fullData = false
                    adapter.update(emptyList(), false)
                    emptyView.showEmptyError(state.error)
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.Data -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter.fullData = false
                    adapter.update(state.data, false)
                    emptyView.visible(false)
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.Refresh -> {
                    swipeToRefresh.isRefreshing = true
                    fullscreenProgressView.visible(false)
                    adapter.fullData = false
                    adapter.update(state.data, false)
                    emptyView.visible(false)
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.NewPageProgress -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter.fullData = false
                    adapter.update(state.data, true)
                    emptyView.visible(false)
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.FullData -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter.fullData = true
                    adapter.update(state.data, false)
                    emptyView.visible(false)
                    swipeToRefresh.visible(true)
                }
            }
        }
    }

    private fun View.visible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}