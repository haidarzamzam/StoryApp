package com.haidev.storyapp.di

import com.haidev.storyapp.ui.screen.login.LoginViewModel
import com.haidev.storyapp.ui.screen.register.RegisterViewModel
import com.haidev.storyapp.ui.screen.splash.SplashViewModel
import com.haidev.storyapp.util.ContextProviders
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(androidApplication()) }
    viewModel { LoginViewModel(androidApplication()) }
    viewModel { RegisterViewModel(androidApplication()) }
}

val apiRepositoryModule = module {
    single { ContextProviders.getInstance() }
}
