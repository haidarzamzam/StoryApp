package com.haidev.storyapp.data.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.haidev.storyapp.data.model.AddStoryModel
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.data.source.local.database.StoryDatabase
import com.haidev.storyapp.data.source.remote.ApiService
import com.haidev.storyapp.data.source.remotemediator.StoryRemoteMediator
import com.haidev.storyapp.di.prefs
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    fun getStory(): LiveData<PagingData<StoryModel.Response.Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getStoryLocation(
    ): StoryModel.Response {
        return apiService.storiesLocationAsync(prefs.prefUserToken.toString()).await()
    }

    suspend fun postStory(
        file: MultipartBody.Part, desc: RequestBody
    ): AddStoryModel.Response {
        return apiService.addStoryAsync(prefs.prefUserToken.toString(), file, desc).await()
    }
}