package com.example.myapplication.presentation.fragments

import android.view.View
import com.demoss.paginatorrenderkit.view.adapter.AbsPaginatorVH
import com.demoss.paginatorrenderkit.view.model.AbsPaginatorItem

class ArticlesHeaderVH(view: View): AbsPaginatorVH<AbsPaginatorItem<ArticlesHeader>>(view) {

    override fun bindData(item: AbsPaginatorItem<ArticlesHeader>, payload: Any) {
        // nothing
    }
}