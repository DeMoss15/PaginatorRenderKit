package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.R
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.PaginatorItem
import com.demoss.paginatorrenderkit.view.model.ProgressItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class ProgressAdapterDelegate : AdapterDelegate<MutableList<PaginatorItem<ProgressItem>>>() {

    override fun isForViewType(items: MutableList<PaginatorItem<ProgressItem>>, position: Int) =
        items[position].isForViewType(ProgressItem::class.java)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_progress))

    override fun onBindViewHolder(
        items: MutableList<PaginatorItem<ProgressItem>>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}