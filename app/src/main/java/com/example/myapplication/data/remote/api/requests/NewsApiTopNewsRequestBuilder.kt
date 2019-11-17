package com.example.myapplication.data.remote.api.requests

import com.example.myapplication.data.remote.api.NewsApiConstants
import com.example.myapplication.data.remote.api.getBaseRequest

class NewsApiTopNewsRequestBuilder {

    var page: Int = 0
    var query: String? = null
    var sources: String? = null
    var category: String? = null
    var country: String? = null

    fun build(): Map<String, String?> = getBaseRequest() + mapOf(NewsApiConstants.PAGE to page.toString()).apply {
        query?.let { this[NewsApiConstants.QUERY] to query }
        query?.let { this[NewsApiConstants.SOURCES] to sources }// source preferred
        query?.let { this[NewsApiConstants.CATEGORY] to category }
        query?.let { if (sources == null )this[NewsApiConstants.COUNTRY] to country }
    }
}