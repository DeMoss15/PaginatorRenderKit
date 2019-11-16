package com.demoss.paginatorrenderkit

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class ProgressAdapterDelegate : AdapterDelegate<MutableList<AbsPaginalItem<ProgressItem>>>() {

    override fun isForViewType(items: MutableList<AbsPaginalItem<ProgressItem>>, position: Int) =
        items[position].isForViewType(ProgressItem::class.java)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_progress))

    override fun onBindViewHolder(
        items: MutableList<AbsPaginalItem<ProgressItem>>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}