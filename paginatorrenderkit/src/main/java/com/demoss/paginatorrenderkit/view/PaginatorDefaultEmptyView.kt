package com.demoss.paginatorrenderkit.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.demoss.paginatorrenderkit.R
import com.demoss.paginatorrenderkit.view.delegate.AbsPaginatorEmptyView

class PaginatorDefaultEmptyView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), AbsPaginatorEmptyView {

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