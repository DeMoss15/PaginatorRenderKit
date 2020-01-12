package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

inline fun <reified T : Any> mutableAdapterDelegate(
    @LayoutRes layout: Int,
    crossinline viewHolderFabric: (View) -> AbsVH<in T>
) = object : AdapterDelegate<MutableList<PaginatorItem<*>>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        viewHolderFabric(parent.inflate(layout))

    override fun isForViewType(items: MutableList<PaginatorItem<*>>, position: Int): Boolean =
        items[position].isForViewType(T::class.java)

    override fun onBindViewHolder(
        items: MutableList<PaginatorItem<*>>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        @Suppress("UNCHECKED_CAST")
        holder as AbsVH<in T>
        holder.bindData(items[position].data as T)
    }
}

abstract class AbsVH<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindData(item: T)
}
