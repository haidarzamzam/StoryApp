package com.haidev.storyapp.data.source.repository

import com.haidev.storyapp.data.model.LoginModel
import com.haidev.storyapp.data.model.RegisterModel
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.data.source.remote.ApiService
import com.haidev.storyapp.di.prefs

class StoryRepository(
    private val apiService: ApiService
) {
    suspend fun postLogin(
        payload: LoginModel.Payload
    ): LoginModel.Response {
        return apiService.loginAsync(payload).await()
    }

    suspend fun postRegister(
        payload: RegisterModel.Payload
    ): RegisterModel.Response {
        return apiService.registerAsync(payload).await()
    }

    suspend fun getStory(
    ): StoryModel.Response {
        return apiService.storiesAsync(prefs.prefUserToken.toString()).await()
    }
}