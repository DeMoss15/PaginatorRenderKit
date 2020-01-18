package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.view.adapter.AbsPaginatorVH
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

//object PaginatorAdapterDelegateFabric {
//
//    inline fun <reified T : Any> create(
//        @LayoutRes layout: Int,
//        crossinline viewHolderFabric: (View) -> AbsPaginatorVH<in T>
//    ) = object : AdapterDelegate<MutableList<PaginatorItem<*>>>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
//            viewHolderFabric(parent.inflate(layout))
//
//        override fun isForViewType(items: MutableList<PaginatorItem<*>>, position: Int): Boolean =
//            items[position].isForViewType(T::class.java)
//
//        override fun onBindViewHolder(
//            items: MutableList<PaginatorItem<*>>,
//            position: Int,
//            holder: RecyclerView.ViewHolder,
//            payloads: MutableList<Any>
//        ) {
//            @Suppress("UNCHECKED_CAST")
//            holder as AbsPaginatorVH<in T>
//            holder.bindData(items[position].data as T)
//        }
//    }
//}


open class PaginatorAdapterDelegate<T : Any> constructor(
    @LayoutRes val layout: Int,
    private val viewHolderFabric: (View) -> AbsPaginatorVH<in T>
) : AdapterDelegate<MutableList<PaginatorItem<*>>>() {

    final override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        viewHolderFabric(parent.inflate(layout))

    final override fun isForViewType(items: MutableList<PaginatorItem<*>>, position: Int): Boolean =
        items[position].isForViewType(T::class.java)

    override fun onBindViewHolder(
        items: MutableList<PaginatorItem<*>>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        @Suppress("UNCHECKED_CAST")
        holder as AbsPaginatorVH<in T>
        @Suppress("UNCHECKED_CAST")
        holder.bindData(items[position].data as T)
    }
}
