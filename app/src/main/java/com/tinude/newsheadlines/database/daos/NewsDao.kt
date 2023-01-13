package com.tinude.newsheadlines.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tinude.newsheadlines.network.response.Article

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(feeds:List<Article>)

    @Query("Select * from Article")
    fun fetchAllFeeds(): List<Article>

}