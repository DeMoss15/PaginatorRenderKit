package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.R
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.PaginatorProgressItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class ProgressAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        items[position] is PaginatorProgressItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_progress))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}