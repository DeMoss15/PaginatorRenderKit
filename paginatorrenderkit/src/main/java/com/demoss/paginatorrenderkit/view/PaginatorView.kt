package com.demoss.paginatorrenderkit.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demoss.paginatorrenderkit.R
import com.demoss.paginatorrenderkit.view.delegate.AbsPaginatorEmptyView
import com.demoss.paginatorrenderkit.view.delegate.AbsPaginatorView
import kotlinx.android.synthetic.main.view_paginator.view.*

class PaginatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), AbsPaginatorView {

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.view_paginator, this, true)
    }

    override fun getRecyclerView(): RecyclerView = recyclerView

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipeToRefresh

    override fun getEmptyView(): AbsPaginatorEmptyView = emptyView

    override fun getFullScreenProgressView(): View = fullscreenProgressView
}