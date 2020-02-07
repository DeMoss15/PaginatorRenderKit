package com.demoss.paginatorrenderkit.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem

object PaginatorDiffItemCallbackFactory {

    val forPaginatorItem by lazy {
        create<AbsPaginatorItem<*>>(
            { oldItem, newItem -> oldItem.areItemsTheSame(newItem) },
            { oldItem, newItem -> oldItem.getChangePayload(newItem) }
        )
    }

    inline fun <reified T : Any> create(
        crossinline areItemsDataTheSame: (oldItem: T, newItem: T) -> Boolean,
        crossinline getChangePayload: (oldItem: T, newItem: T) -> Any = { _, _ -> Any() }
    ) = object : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean =
            if (oldItem === newItem) {
                true
            } else if (oldItem is T && newItem is T) {
                areItemsDataTheSame(oldItem, newItem)
            } else {
                false
            }

        override fun getChangePayload(oldItem: Any, newItem: Any) =
            if (oldItem is T && newItem is T) {
                getChangePayload(oldItem, newItem)
            } else {
                Any()
            }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) =
            oldItem == newItem
    }

}
