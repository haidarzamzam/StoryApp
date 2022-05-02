package com.haidev.storyapp.data.source.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haidev.storyapp.data.model.StoryModel

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryModel.Response.Story>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryModel.Response.Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}