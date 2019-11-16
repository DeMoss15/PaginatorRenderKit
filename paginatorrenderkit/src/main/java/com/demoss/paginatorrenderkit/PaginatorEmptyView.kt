package com.demoss.paginatorrenderkit

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

abstract class PaginatorEmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    abstract fun showEmptyData()
    abstract fun showEmptyError(error: Throwable)
}