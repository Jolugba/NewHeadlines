package com.tinude.newsheadlines.network

import com.tinude.newsheadlines.network.response.NewsApiResponse
import com.tinude.newsheadlines.util.Constants.BASE.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getNewsHeadlines(
        @Query("country") country: String="us",
        @Query("apiKey") apiKey: String = API_KEY,
    ): NewsApiResponse


}