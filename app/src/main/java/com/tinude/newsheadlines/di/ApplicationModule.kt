package com.tinude.newsheadlines.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.tinude.newsheadlines.BuildConfig
import com.tinude.newsheadlines.database.AppRoomDatabase
import com.tinude.newsheadlines.database.AppRoomDatabase.Companion.getInstance
import com.tinude.newsheadlines.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideInterceptor():Interceptor{
       val interceptor = Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            val url = request.url
            val builder = url.newBuilder()
            requestBuilder.url(builder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
            chain.proceed(requestBuilder.build())

        }
        return interceptor
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        loggingInterceptor: HttpLoggingInterceptor,
    ): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(provideInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder().baseUrl(APP_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val logLevel =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = logLevel
        return loggingInterceptor
    }


    companion object {
        const val APP_BASE_URL = Constants.BASE.BASE_URL
    }

    @Provides
    @Singleton
    fun providesRoomDatabase(
        @ApplicationContext applicationContext: Context
    ): AppRoomDatabase {
        return getInstance(applicationContext)!!
    }
}
