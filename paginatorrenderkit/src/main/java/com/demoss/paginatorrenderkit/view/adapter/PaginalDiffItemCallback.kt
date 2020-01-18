package com.demoss.paginatorrenderkit.view.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem

object PaginalDiffItemCallback : DiffUtil.ItemCallback<AbsPaginatorItem<*>>() {
    override fun areItemsTheSame(
        oldItem: AbsPaginatorItem<*>,
        newItem: AbsPaginatorItem<*>
    ): Boolean {
        if (oldItem === newItem) return true
        return oldItem.areItemsTheSame(newItem)
    }

    override fun getChangePayload(
        oldItem: AbsPaginatorItem<*>,
        newItem: AbsPaginatorItem<*>
    ) = oldItem.getChangePayload(newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: AbsPaginatorItem<*>,
        newItem: AbsPaginatorItem<*>
    ) = oldItem == newItem
}