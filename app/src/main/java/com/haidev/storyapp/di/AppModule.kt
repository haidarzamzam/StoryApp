package com.haidev.storyapp.di

import com.haidev.storyapp.data.source.local.sharepref.PrefManager
import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.data.source.repository.UserRepository
import com.haidev.storyapp.ui.screen.login.LoginViewModel
import com.haidev.storyapp.ui.screen.register.RegisterViewModel
import com.haidev.storyapp.ui.screen.splash.SplashViewModel
import com.haidev.storyapp.ui.screen.story.AddStoryViewModel
import com.haidev.storyapp.ui.screen.story.DetailStoryViewModel
import com.haidev.storyapp.ui.screen.story.StoryViewModel
import com.haidev.storyapp.util.ContextProviders
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(androidApplication()) }
    viewModel { LoginViewModel(get(), androidApplication()) }
    viewModel { RegisterViewModel(get(), androidApplication()) }
    viewModel { StoryViewModel(get(), androidApplication()) }
    viewModel { DetailStoryViewModel(androidApplication()) }
    viewModel { AddStoryViewModel(get(), androidApplication()) }
}

val apiRepositoryModule = module {
    single { ContextProviders.getInstance() }
    single { UserRepository(get()) }
    single { StoryRepository(get(), get()) }
}

val prefsModule = module {
    single { PrefManager() }
}