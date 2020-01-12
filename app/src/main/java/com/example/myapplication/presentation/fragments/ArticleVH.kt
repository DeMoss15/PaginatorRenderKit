package com.example.myapplication.presentation.fragments

import android.view.View
import com.demoss.paginatorrenderkit.view.adapter.delegate.AbsVH
import com.example.myapplication.domain.model.Article
import com.example.myapplication.util.showImage
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleVH(view: View): AbsVH<Article>(view) {
    override fun bindData(item: Article) {
        itemView.apply {
            tvTitle.text = item.title
            tvDate.text = item.publishedAt
            tvDescription.text = item.description
            author.text = item.author
            ivPhoto.showImage(item.image)
        }
    }
}