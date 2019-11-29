package com.demoss.paginatorrenderkit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface PaginatorView {

    fun getRecyclerView(): RecyclerView
    fun getSwipeToRefresh(): SwipeRefreshLayout
    fun getEmptyView(): PaginatorEmptyView
    fun getFullScreenProgressView(): View
}