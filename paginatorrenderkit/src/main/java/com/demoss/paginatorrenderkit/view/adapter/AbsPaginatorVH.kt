package com.demoss.paginatorrenderkit.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbsPaginatorVH<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindData(item: T)
}