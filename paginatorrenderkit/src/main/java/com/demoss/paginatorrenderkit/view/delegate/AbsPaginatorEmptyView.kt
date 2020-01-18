package com.demoss.paginatorrenderkit.view.delegate

interface AbsPaginatorEmptyView {
    fun showEmptyData()
    fun showEmptyError(error: Throwable)
    fun visible(isVisible: Boolean)
}