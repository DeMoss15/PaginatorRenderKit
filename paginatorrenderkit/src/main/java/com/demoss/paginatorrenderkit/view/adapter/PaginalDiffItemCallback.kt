package com.demoss.paginatorrenderkit.view.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

object PaginatorDiffItemCallbackFabric {

    inline fun <reified T : Any> create(
        crossinline areItemsDataTheSame: (oldItem: T, newItem: T) -> Boolean,
        crossinline getChangePayload: (oldItem: T, newItem: T) -> Any = { _, _ -> Any() }
    ) = object : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            if (oldItem === newItem) return true
            return if (oldItem is T && newItem is T) areItemsDataTheSame(oldItem, newItem)
            else false
        }

        override fun getChangePayload(
            oldItem: Any,
            newItem: Any
        ) = if (oldItem is T && newItem is T) getChangePayload(oldItem, newItem)
        else Any()

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ) = oldItem == newItem
    }
}