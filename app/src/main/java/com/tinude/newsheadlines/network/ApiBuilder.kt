package com.tinude.newsheadlines.network

import retrofit2.Retrofit
import javax.inject.Inject

open class NetworkService @Inject constructor(private val retrofit: Retrofit) {
        val api: ApiService
            get() = retrofit.create(ApiService::class.java)
}

