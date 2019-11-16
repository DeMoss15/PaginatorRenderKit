package com.demoss.paginatorrenderkit

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.android.synthetic.main.view_paginator.view.*

class PaginatorViewDelegate(
    private var refreshCallback: (() -> Unit)? = null,
    private var nextPageCallback: (() -> Unit)? = null,
    private var recyclerView: RecyclerView,
    private var swipeToRefresh: SwipeRefreshLayout,
    private var emptyView: PaginatorEmptyView,
    private var fullscreenProgressView: View,
    vararg delegate: AdapterDelegate<MutableList<AbsPaginalItem<*>>>
) {

    constructor(
        refreshCallback: (() -> Unit)? = null,
        nextPageCallback: (() -> Unit)? = null,
        paginatorView: PaginatorView,
        vararg delegate: AdapterDelegate<MutableList<AbsPaginalItem<*>>>
    ) : this(
        refreshCallback,
        nextPageCallback,
        paginatorView.recyclerView,
        paginatorView.swipeToRefresh,
        paginatorView.emptyView as PaginatorEmptyView,
        paginatorView.fullscreenProgressView,
        *delegate
    )

    private var adapter: PaginalAdapter = PaginalAdapter(*delegate)

    init {
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@PaginatorViewDelegate.adapter
        }
    }

    fun render(state: Paginator.State<AbsPaginalItem<*>>) {
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

    inner class PaginalAdapter(vararg delegate: AdapterDelegate<MutableList<AbsPaginalItem<*>>>) :
        AsyncListDifferDelegationAdapter<AbsPaginalItem<*>>(
            object : DiffUtil.ItemCallback<AbsPaginalItem<*>>() {
                override fun areItemsTheSame(
                    oldItem: AbsPaginalItem<*>,
                    newItem: AbsPaginalItem<*>
                ): Boolean {
                    if (oldItem === newItem) return true
                    return oldItem.areItemsTheSame(newItem)
                }

                override fun getChangePayload(
                    oldItem: AbsPaginalItem<*>,
                    newItem: AbsPaginalItem<*>
                ) = Any() // disable default blink animation

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: AbsPaginalItem<*>,
                    newItem: AbsPaginalItem<*>
                ) = oldItem == newItem
            }
        ) {
        var fullData = false

        init {
            items = mutableListOf()
            @Suppress("UNCHECKED_CAST")
            delegatesManager.addDelegate(ProgressAdapterDelegate() as AdapterDelegate<MutableList<AbsPaginalItem<*>>>)
            delegate.forEach { delegatesManager.addDelegate(it) }
        }

        fun update(data: List<AbsPaginalItem<*>>, isPageProgress: Boolean) {
            items = mutableListOf<AbsPaginalItem<*>>().apply {
                addAll(data)
                if (isPageProgress) add(AbsPaginalItem(ProgressItem) { true })
            }
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
            payloads: MutableList<Any?>
        ) {
            super.onBindViewHolder(holder, position, payloads)
            if (!fullData && position >= items.size - 3) nextPageCallback?.invoke()
        }
    }
}