package com.haidev.storyapp.data.source.remote

import com.haidev.storyapp.data.model.LoginModel
import com.haidev.storyapp.data.model.RegisterModel
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun loginAsync(
        @Body payload: LoginModel.Payload
    ): Deferred<LoginModel.Response>

    @POST("register")
    fun registerAsync(
        @Body payload: RegisterModel.Payload
    ): Deferred<RegisterModel.Response>
}