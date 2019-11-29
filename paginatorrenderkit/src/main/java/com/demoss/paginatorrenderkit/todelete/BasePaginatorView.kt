package com.demoss.paginatorrenderkit.todelete

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demoss.paginatorrenderkit.PaginatorEmptyView
import com.demoss.paginatorrenderkit.PaginatorView
import com.demoss.paginatorrenderkit.R
import kotlinx.android.synthetic.main.view_paginator.view.*

class BasePaginatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), PaginatorView {

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.view_paginator, this, true)
    }

    override fun getRecyclerView(): RecyclerView = recyclerView

    override fun getSwipeToRefresh(): SwipeRefreshLayout = swipeToRefresh

    override fun getEmptyView(): PaginatorEmptyView = emptyView

    override fun getFullScreenProgressView(): View = fullscreenProgressView
}