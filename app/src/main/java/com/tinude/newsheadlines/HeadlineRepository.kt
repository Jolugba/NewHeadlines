package com.tinude.newsheadlines

import android.app.Application
import com.tinude.newsheadlines.database.AppRoomDatabase
import com.tinude.newsheadlines.network.ApiBuilder
import com.tinude.newsheadlines.network.ResultWrapper
import com.tinude.newsheadlines.network.response.Article
import com.tinude.newsheadlines.network.response.NewsApiResponse
import com.tinude.newsheadlines.network.safeApiCall

class HeadlineRepository(application: Application) {

    private val newsDao by lazy {
        AppRoomDatabase.getInstance(application)!!.newsDao()
    }
    private fun insertNews(feeds: List<Article>) {
        newsDao.insertNews(feeds)
    }

    suspend fun fetchNewsHeadlines() {
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

    suspend fun getNews(): NewsApiResponse {
        return service.getNewsHeadlines(country="us")
    }
    private val service by lazy {
        ApiBuilder.service
    }
    private fun getCachedNewsOnce() = newsDao.fetchAllFeeds()

    suspend fun fetchNews(): ResultWrapper.Success<List<Article>> {
        val news = getCachedNewsOnce()
        if (news.isEmpty()) {
           fetchNewsHeadlines()
        }
        return ResultWrapper.Success(news)
    }


}