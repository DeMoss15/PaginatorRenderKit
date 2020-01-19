package com.demoss.paginatorrenderkit.view.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demoss.paginatorrenderkit.Paginator

/**
 * Delegate for setup views in accordance to current @link[Paginator.State]
 *
 * @param refreshCallback triggered by @param[swipeToRefresh]
 * @param adapter will be set to recycler view and notified about new data form new state @see[render]
 * @param emptyView displays empty data and empty error states
 *
 * @author Daniel Mossur
 */

class PaginatorViewDelegate<T>(
    refreshCallback: (() -> Unit)? = null,
    private val adapter: AbsPaginatorAdapter<T>,
    private val recyclerView: RecyclerView,
    private val swipeToRefresh: SwipeRefreshLayout,
    private val emptyView: AbsPaginatorEmptyView,
    private val fullscreenProgressView: View
) {

    /**
     * @param refreshCallback triggered by @param swipeToRefresh
     * @param adapter will be set to recycler view and notified about new data form new state @see[render]
     * @param paginatorView provides: @property[recyclerView], @property[swipeToRefresh], @property[emptyView], @property[fullscreenProgressView]
     * @constructor An alternative to primary constructor
     */
    constructor(
        refreshCallback: (() -> Unit)? = null,
        adapter: AbsPaginatorAdapter<T>,
        paginatorView: AbsPaginatorView
    ) : this(
        refreshCallback,
        adapter,
        paginatorView.getRecyclerView(),
        paginatorView.getSwipeRefreshLayout(),
        paginatorView.getEmptyView(),
        paginatorView.getFullScreenProgressView()
    )

    init {
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        recyclerView.adapter = adapter
    }

    /**
     * The only and the main purpose of the delegate - setup views depending on new state
     *
     * @param state could be received from @see[Paginator.Store.render]
     */
    fun render(state: Paginator.State<T>) {
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