package com.demoss.paginatorrenderkit.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.view.adapter.delegate.ProgressAdapterDelegate
import com.demoss.paginatorrenderkit.view.delegate.AbsPaginatorAdapter
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem
import com.demoss.paginatorrenderkit.view.model.PaginatorProgressItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

/**
 * An implementation of @see[AbsPaginatorAdapter],
 * [AbsPaginatorItem]-oriented solution
 *
 */
class PaginatorAdapter(
    private val nextPageCallback: (() -> Unit)?,
    diffItemCallback: DiffUtil.ItemCallback<Any>,
    vararg delegate: AdapterDelegate<out MutableList<out Any>>
) : AbsPaginatorAdapter<Any>(diffItemCallback) {

    companion object {
        const val NEXT_PAGE_REQUEST_OFFSET = 3
    }

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(ProgressAdapterDelegate())
        @Suppress("UNCHECKED_CAST")
        delegate.forEach { delegatesManager.addDelegate(it as AdapterDelegate<MutableList<Any>>) }
    }

    override fun update(data: List<Any>, isPageProgress: Boolean) {
        items = mutableListOf<Any>().apply {
            addAll(data)
            if (isPageProgress) add(PaginatorProgressItem)
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