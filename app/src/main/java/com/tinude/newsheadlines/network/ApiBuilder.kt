package com.tinude.newsheadlines.network

import com.google.gson.GsonBuilder
import com.tinude.newsheadlines.BuildConfig
import com.tinude.newsheadlines.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiBuilder {

    private val interceptor = Interceptor { chain ->
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val url = request.url
        val builder = url.newBuilder()
        requestBuilder.url(builder.build())
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
        chain.proceed(requestBuilder.build())

    }

    private fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val logLevel =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = logLevel
        return loggingInterceptor
    }

  private fun provideRetrofit(
    ): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(providesLoggingInterceptor())
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().baseUrl(Constants.AppConstants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    var service: ApiService = provideRetrofit().create(ApiService::class.java)
}