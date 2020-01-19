package com.demoss.paginatorrenderkit.view.model

interface AbsPaginatorItem<T: Any> {

    val data: T

    // for adapter delegate ===================================
    fun isForViewType(viewType: Class<*>): Boolean

    // for diff item callback =================================
    fun areItemsTheSame(newItem: AbsPaginatorItem<*>): Boolean

    fun getChangePayload(newItem: AbsPaginatorItem<*>): Any = Any()

    fun areContentsTheSame(newItem: AbsPaginatorItem<*>): Boolean = data == newItem.data
}