package com.demoss.paginatorrenderkit.view.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface AbsPaginatorView {

    fun getRecyclerView(): RecyclerView
    fun getSwipeRefreshLayout(): SwipeRefreshLayout
    fun getEmptyView(): AbsPaginatorEmptyView
    fun getFullScreenProgressView(): View
}