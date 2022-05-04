package com.haidev.storyapp.data.source.remote

import com.haidev.storyapp.data.model.AddStoryModel
import com.haidev.storyapp.data.model.LoginModel
import com.haidev.storyapp.data.model.RegisterModel
import com.haidev.storyapp.data.model.StoryModel
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun loginAsync(
        @Body payload: LoginModel.Payload
    ): Deferred<LoginModel.Response>

    @POST("register")
    fun registerAsync(
        @Body payload: RegisterModel.Payload
    ): Deferred<RegisterModel.Response>

    @GET("stories")
    fun storiesAsync(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Deferred<StoryModel.Response>

    @GET("stories")
    fun storiesLocationAsync(
        @Header("Authorization") token: String,
        @Query("location") page: Int = 1
    ): Deferred<StoryModel.Response>

    @Multipart
    @POST("stories")
    fun addStoryAsync(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Deferred<AddStoryModel.Response>
}