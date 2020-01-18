package com.demoss.paginatorrenderkit.view.model

object PaginatorProgress

object ProgressItem: AbsPaginatorItem<PaginatorProgress> {

    override val data: PaginatorProgress = PaginatorProgress

    override fun areItemsTheSame(other: AbsPaginatorItem<*>): Boolean = other.data == data

    override fun isForViewType(viewType: Class<*>): Boolean = viewType == PaginatorProgress::class.java

    override fun getChangePayload(newItem: AbsPaginatorItem<*>): Any = Any()

}