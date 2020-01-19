package com.demoss.paginatorrenderkit.view.delegate

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * The abstract recycler view adapter for @see[PaginatorViewDelegate]
 * created for more scalability
 *
 * [AsyncListDifferDelegationAdapter] is a part of [adapterdelegates4] library
 * all links are provided in @see[README.md]
 * or @see[https://github.com/DeMoss15/PaginatorRenderKit]
 *
 * @author Daniel Mossur
 */
abstract class AbsPaginatorAdapter<T>(
    diffItemCallback: DiffUtil.ItemCallback<T>
) : AsyncListDifferDelegationAdapter<T>(diffItemCallback) {

    open var fullData = false

    abstract fun update(data: List<T>, isPageProgress: Boolean)
}