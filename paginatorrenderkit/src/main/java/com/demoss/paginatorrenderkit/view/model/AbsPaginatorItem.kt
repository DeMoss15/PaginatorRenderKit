package com.demoss.paginatorrenderkit.view.model

interface AbsPaginatorItem<T: Any> {

    val data: T

    fun areItemsTheSame(other: AbsPaginatorItem<*>): Boolean

    fun isForViewType(viewType: Class<*>): Boolean

    fun getChangePayload(newItem: AbsPaginatorItem<*>): Any
}