package com.demoss.paginatorrenderkit.view.delegate

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demoss.paginatorrenderkit.Paginator
import com.demoss.paginatorrenderkit.view.PaginatorView
import com.demoss.paginatorrenderkit.view.adapter.PaginalAdapter
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.view_paginator.view.*

class PaginatorViewDelegate(
    private var refreshCallback: (() -> Unit)? = null,
    nextPageCallback: (() -> Unit)? = null,
    private var recyclerView: RecyclerView,
    private var swipeToRefresh: SwipeRefreshLayout,
    private var emptyView: PaginatorEmptyView,
    private var fullscreenProgressView: View,
    vararg delegate: AdapterDelegate<MutableList<PaginatorItem<*>>>
) {

    constructor(
        refreshCallback: (() -> Unit)? = null,
        nextPageCallback: (() -> Unit)? = null,
        paginatorView: PaginatorView,
        vararg delegate: AdapterDelegate<MutableList<PaginatorItem<*>>>
    ) : this(
        refreshCallback,
        nextPageCallback,
        paginatorView.recyclerView,
        paginatorView.swipeToRefresh,
        paginatorView.emptyView as PaginatorEmptyView,
        paginatorView.fullscreenProgressView,
        *delegate
    )

    private var adapter: PaginalAdapter = PaginalAdapter(nextPageCallback, *delegate)

    init {
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@PaginatorViewDelegate.adapter
        }
        refreshCallback?.invoke()
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