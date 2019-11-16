package com.demoss.paginatorrenderkit

import android.content.Context
import android.util.AttributeSet

class PaginatorDefaultEmptyView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PaginatorEmptyView(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_empty, this)
    }

    override fun showEmptyData() {
        // nothing
    }

    override fun showEmptyError(error: Throwable) {
        // nothing
    }
}