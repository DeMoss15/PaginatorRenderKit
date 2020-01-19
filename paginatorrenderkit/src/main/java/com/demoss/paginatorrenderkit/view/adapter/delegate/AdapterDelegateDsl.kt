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

    inline fun <reified T : Any> create(
        @LayoutRes layout: Int,
        crossinline customViewTypePredicate: (item: T) -> Boolean = { _ -> true},
        crossinline viewHolderFabric: (View) -> AbsPaginatorVH<in T>
    ) = object : AdapterDelegate<MutableList<AbsPaginatorItem<*>>>() {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            viewHolderFabric(parent.inflate(layout))

        override fun isForViewType(items: MutableList<AbsPaginatorItem<*>>, position: Int): Boolean =
            items[position].isForViewType(T::class.java) && customViewTypePredicate((items[position].data) as T)

        override fun onBindViewHolder(
            items: MutableList<AbsPaginatorItem<*>>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
        ) {
            @Suppress("UNCHECKED_CAST")
            holder as AbsPaginatorVH<in T>
            holder.bindData(items[position].data as T)
        }
    }
}
