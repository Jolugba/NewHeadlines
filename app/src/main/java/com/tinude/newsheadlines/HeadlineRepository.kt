package com.tinude.newsheadlines

import com.tinude.newsheadlines.database.AppRoomDatabase
import com.tinude.newsheadlines.network.NetworkService
import com.tinude.newsheadlines.network.ResultWrapper
import com.tinude.newsheadlines.network.response.Article
import com.tinude.newsheadlines.network.response.NewsApiResponse
import com.tinude.newsheadlines.network.safeApiCall
import javax.inject.Inject

class HeadlineRepository @Inject constructor(
    private val database: AppRoomDatabase,
    private val networkService: NetworkService,
) {


    private fun insertNews(feeds: List<Article>) {
        database.newsDao().insertNews(feeds)
    }

   private suspend fun fetchNewsHeadlines() {
        val apiCall = safeApiCall {
            getNews()
        }
        when (apiCall) {
            is ResultWrapper.Success -> {
                insertNews(apiCall.value.articles)
            }
            else -> {}
        }
    }

    private suspend fun getNews(): NewsApiResponse {
        return networkService.api.getNewsHeadlines()
    }
    private fun getCachedNewsOnce() =  database.newsDao().fetchAllFeeds()

    suspend fun fetchNews(): ResultWrapper.Success<List<Article>> {
        val news = getCachedNewsOnce()
        if (news.isEmpty()) {
           fetchNewsHeadlines()
        }
        return ResultWrapper.Success(news)
    }


}