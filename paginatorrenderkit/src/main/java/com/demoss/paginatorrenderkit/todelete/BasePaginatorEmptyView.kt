package com.demoss.paginatorrenderkit.todelete

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.demoss.paginatorrenderkit.PaginatorEmptyView
import com.demoss.paginatorrenderkit.R

class BasePaginatorEmptyView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), PaginatorEmptyView {

    init {
        inflate(context, R.layout.view_empty, this)
    }

    override fun showEmptyData() {
        // nothing
    }

    override fun showEmptyError(error: Throwable) {
        // nothing
    }

    override fun visible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}