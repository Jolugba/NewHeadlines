package com.tinude.newsheadlines.network.response

data class NewsApiResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)