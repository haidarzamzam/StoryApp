package com.haidev.storyapp.di

import com.haidev.storyapp.BuildConfig
import com.haidev.storyapp.data.source.remote.provideApi
import com.haidev.storyapp.data.source.remote.provideCacheInterceptor
import com.haidev.storyapp.data.source.remote.provideHttpLoggingInterceptor
import com.haidev.storyapp.data.source.remote.provideMoshiConverter

import org.koin.dsl.module

val remoteModule = module {
    single { provideCacheInterceptor() }
    single { provideHttpLoggingInterceptor() }
    single { provideMoshiConverter() }
    single {
        provideApi(
            BuildConfig.API_URL,
            get()
        )
    }
}
