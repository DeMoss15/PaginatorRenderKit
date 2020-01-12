package com.demoss.paginatorrenderkit.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.view.adapter.delegate.ProgressAdapterDelegate
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.demoss.paginatorrenderkit.view.model.ProgressItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class PaginatorAdapter(
    private val nextPageCallback: (() -> Unit)?,
    vararg delegate: AdapterDelegate<MutableList<PaginatorItem<*>>>
) : AsyncListDifferDelegationAdapter<PaginatorItem<*>>(PaginalDiffItemCallback) {

    companion object {
        const val NEXT_PAGE_REQUEST_OFFSET = 3
    }

    var fullData = false

    init {
        items = mutableListOf()
        @Suppress("UNCHECKED_CAST")
        delegatesManager.addDelegate(ProgressAdapterDelegate() as AdapterDelegate<MutableList<PaginatorItem<*>>>)
        delegate.forEach { delegatesManager.addDelegate(it) }
    }

    fun update(data: List<PaginatorItem<*>>, isPageProgress: Boolean) {
        items = mutableListOf<PaginatorItem<*>>().apply {
            addAll(data)
            if (isPageProgress) add(PaginatorItem(ProgressItem) { true })
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (!fullData && position >= items.size - NEXT_PAGE_REQUEST_OFFSET)
            nextPageCallback?.invoke()
    }
}