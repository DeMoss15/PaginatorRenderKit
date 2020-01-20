package com.example.myapplication.presentation.fragments

import android.view.View
import com.demoss.paginatorrenderkit.view.adapter.AbsPaginatorVH

class ArticlesHeaderVH(view: View): AbsPaginatorVH<ArticlesHeader>(view) {

    override fun bindData(item: ArticlesHeader, payload: Any) {
        // nothing
    }
}