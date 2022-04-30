package com.haidev.storyapp.di

import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.data.source.sharepref.PrefManager
import com.haidev.storyapp.ui.screen.login.LoginViewModel
import com.haidev.storyapp.ui.screen.register.RegisterViewModel
import com.haidev.storyapp.ui.screen.splash.SplashViewModel
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
}

val apiRepositoryModule = module {
    single { ContextProviders.getInstance() }
    single { StoryRepository(get()) }
}

val prefsModule = module {
    single { PrefManager() }
}