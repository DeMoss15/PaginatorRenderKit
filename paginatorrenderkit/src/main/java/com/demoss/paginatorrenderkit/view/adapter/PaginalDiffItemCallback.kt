package com.demoss.paginatorrenderkit.view.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.demoss.paginatorrenderkit.view.model.PaginatorItem

object PaginalDiffItemCallback : DiffUtil.ItemCallback<PaginatorItem<*>>() {
    override fun areItemsTheSame(
        oldItem: PaginatorItem<*>,
        newItem: PaginatorItem<*>
    ): Boolean {
        if (oldItem === newItem) return true
        return oldItem.areItemsTheSame(newItem)
    }

    override fun getChangePayload(
        oldItem: PaginatorItem<*>,
        newItem: PaginatorItem<*>
    ) = Any() // disable default blink animation

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: PaginatorItem<*>,
        newItem: PaginatorItem<*>
    ) = oldItem == newItem
}