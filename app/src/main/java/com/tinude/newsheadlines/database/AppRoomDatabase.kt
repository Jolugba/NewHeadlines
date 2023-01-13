package com.tinude.newsheadlines.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tinude.newsheadlines.database.daos.NewsDao
import com.tinude.newsheadlines.BuildConfig
import com.tinude.newsheadlines.network.response.Article

/**
 * Author: Omolara Adejuwon
 * Date: 30/04/2020.
 */
@Database(entities = [Article::class], version = 2, exportSchema = false)

abstract class AppRoomDatabase : RoomDatabase() {
    companion object {
        private val DATABASE_NAME =
            if (BuildConfig.DEBUG) "news_db" else "news-headline_dp"
        private var sInstance: AppRoomDatabase? = null
        fun getInstance(context: Context): AppRoomDatabase? {
            if (sInstance == null) {
                synchronized(AppRoomDatabase::class.java) {
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return sInstance
        }
    }

    abstract fun newsDao(): NewsDao
}
