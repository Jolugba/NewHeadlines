package com.tinude.newsheadlines.network

import com.tinude.newsheadlines.BuildConfig
import com.tinude.newsheadlines.network.response.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getNewsHeadlines(
        @Query("country") country: String="us",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY_NEWS,
    ): NewsApiResponse


}