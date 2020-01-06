package com.demoss.paginatorrenderkit.view.model

class PaginatorItem<T : Any>(
    val data: T,
    private val areDataTheSame: (other: T) -> Boolean
) {

    @Suppress("UNCHECKED_CAST")
    fun areItemsTheSame(other: PaginatorItem<*>): Boolean =
        other.data.javaClass == data.javaClass && areDataTheSame(other.data as T)

    fun isForViewType(viewType: Class<*>): Boolean = viewType.isInstance(data)
}
