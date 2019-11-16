package com.demoss.paginatorrenderkit

class AbsPaginalItem<T : Any>(
    val data: T,
    private val areDataTheSame: (other: T) -> Boolean
) {
    private val dataClazz = data::class.java
    fun areItemsTheSame(other: AbsPaginalItem<*>): Boolean = other.dataClazz == dataClazz && areDataTheSame(other.data as T)
    fun isForViewType(viewType: Class<*>): Boolean = viewType == dataClazz
    inline fun <reified T> isChildOf(): Boolean = data is T
}
