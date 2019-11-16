package com.demoss.paginatorrenderkit

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

inline fun <reified T : Any> mutableAdapterDelegate(
    @LayoutRes layout: Int,
//    customTypes: List<Class<*>> = listOf(),
    crossinline viewHolderFabric: (View) -> AbsVH<in T>
) = object : AdapterDelegate<MutableList<AbsPaginalItem<*>>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        viewHolderFabric(parent.inflate(layout))

    // /////////////////////////////////////////////////////////////////////////
    // The adapter delegate will be used for T type and for it's child [AbsPaginalItem.isChildOf()].
    // in case of need to make exception for this rule use commented code with customTypes
    // ////////////////////////////////////////////////////////////////////////
    override fun isForViewType(items: MutableList<AbsPaginalItem<*>>, position: Int): Boolean =
        items[position].isForViewType(T::class.java) || items[position].isChildOf<T>()/*||
                customTypes.find { items[position].isForViewType(it) } != null*/

    override fun onBindViewHolder(
        items: MutableList<AbsPaginalItem<*>>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        @Suppress("UNCHECKED_CAST")
        holder as AbsVH<in T>
        holder.bindData(items[position].data as T)
    }
}

abstract class AbsVH<T>(val view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindData(item: T)
}
