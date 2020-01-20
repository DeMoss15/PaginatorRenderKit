package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.view.adapter.AbsPaginatorVH
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

object PaginatorAdapterDelegateFabric {

    fun <T : Any> create(
        @LayoutRes layout: Int,
        customViewTypePredicate: (item: T) -> Boolean = { _ -> true},
        viewHolderFabric: (View) -> AbsPaginatorVH<in T>
    ) = object : AdapterDelegate<MutableList<T>>() {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            viewHolderFabric(parent.inflate(layout))

        override fun isForViewType(items: MutableList<T>, position: Int): Boolean =
            customViewTypePredicate(items[position])

        override fun onBindViewHolder(
            items: MutableList<T>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
        ) {
            @Suppress("UNCHECKED_CAST")
            holder as AbsPaginatorVH<in T>
            holder.bindData(
                items[position],
                payloads.takeIf { it.isNotEmpty() }
                    ?.get(0) ?: Unit
            )
        }
    }

    inline fun <reified T : Any> createForPaginatorItem(
        @LayoutRes layout: Int,
        crossinline customViewTypePredicate: (item: T) -> Boolean = { _ -> true},
        crossinline viewHolderFabric: (View) -> AbsPaginatorVH<in T>
    ) = object : AdapterDelegate<MutableList<AbsPaginatorItem<T>>>() {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            viewHolderFabric(parent.inflate(layout))

        override fun isForViewType(items: MutableList<AbsPaginatorItem<T>>, position: Int): Boolean =
            items[position].isForViewType(T::class.java) && customViewTypePredicate(items[position].data)

        override fun onBindViewHolder(
            items: MutableList<AbsPaginatorItem<T>>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
        ) {
            @Suppress("UNCHECKED_CAST")
            holder as AbsPaginatorVH<in T>
            holder.bindData(
                items[position].data,
                payloads.takeIf { it.isNotEmpty() }
                    ?.get(0) ?: Unit
            )
        }
    }
}
