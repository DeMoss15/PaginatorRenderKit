package com.demoss.paginatorrenderkit.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demoss.paginatorrenderkit.R
import com.demoss.paginatorrenderkit.view.inflate
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem
import com.demoss.paginatorrenderkit.view.model.PaginatorProgress
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class ProgressAdapterDelegate : AdapterDelegate<MutableList<AbsPaginatorItem<PaginatorProgress>>>() {

    override fun isForViewType(items: MutableList<AbsPaginatorItem<PaginatorProgress>>, position: Int) =
        items[position].isForViewType(PaginatorProgress::class.java)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_progress))

    override fun onBindViewHolder(
        items: MutableList<AbsPaginatorItem<PaginatorProgress>>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}