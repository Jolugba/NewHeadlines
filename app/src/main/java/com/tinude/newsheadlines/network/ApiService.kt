package com.tinude.newsheadlines.network

import com.tinude.newsheadlines.network.response.NewsApiResponse
import com.tinude.newsheadlines.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getNewsHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = Constants.AppConstants.API_KEY,
    ): NewsApiResponse

}