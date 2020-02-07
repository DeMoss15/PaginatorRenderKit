package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.view.adapter.AbsPaginatorVH
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

object PaginatorAdapterDelegateFactory {

    inline fun <reified T : Any> create(
        @LayoutRes layout: Int,
        crossinline customViewTypePredicate: (item: T) -> Boolean = { _ -> true},
        crossinline viewHolderFabric: (View) -> AbsPaginatorVH<in T>
    ) = object : AdapterDelegate<MutableList<Any>>() {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            viewHolderFabric(parent.inflate(layout))

        override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
            items[position] is T && customViewTypePredicate(items[position] as T)

        override fun onBindViewHolder(
            items: MutableList<Any>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
        ) {
            @Suppress("UNCHECKED_CAST")
            holder as AbsPaginatorVH<in T>
            holder.bindData(
                items[position] as T,
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
