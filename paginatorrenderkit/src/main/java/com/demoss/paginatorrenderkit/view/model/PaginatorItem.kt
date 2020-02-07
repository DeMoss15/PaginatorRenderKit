package com.demoss.paginatorrenderkit.view.model

class PaginatorItem<T : Any>(
    override val data: T,
    private val areDataTheSame: (other: T) -> Boolean
): AbsPaginatorItem<T> {

    @Suppress("UNCHECKED_CAST")
    override fun areItemsTheSame(newItem: AbsPaginatorItem<*>): Boolean =
        newItem.data.javaClass == data.javaClass && areDataTheSame(newItem.data as T)

    override fun isForViewType(viewType: Class<*>): Boolean = viewType.isInstance(data)

    override fun getChangePayload(newItem: AbsPaginatorItem<*>): Any = Any() // no animation
}
