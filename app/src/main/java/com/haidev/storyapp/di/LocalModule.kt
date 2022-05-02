package com.haidev.storyapp.di

import com.haidev.storyapp.data.source.local.database.StoryDatabase
import com.haidev.storyapp.data.source.local.database.dao.RemoteKeysDao
import com.haidev.storyapp.data.source.local.database.dao.StoryDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localModule = module {
    fun storyDao(database: StoryDatabase): StoryDao = database.storyDao()
    fun remoteKeysDao(database: StoryDatabase): RemoteKeysDao = database.remoteKeysDao()

    single { StoryDatabase.getDatabase(androidApplication()) }
    single { storyDao(get()) }
    single { remoteKeysDao(get()) }
}