package com.demoss.paginatorrenderkit

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

interface PaginatorEmptyView {
    fun showEmptyData()
    fun showEmptyError(error: Throwable)
    fun visible(isVisible: Boolean)
}