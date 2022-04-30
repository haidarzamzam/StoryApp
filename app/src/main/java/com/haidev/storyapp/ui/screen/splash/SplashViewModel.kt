package com.haidev.storyapp.ui.screen.splash

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashViewModel(application: Application) :
    BaseViewModel<SplashNavigator>(application) {

    fun displaySplashAsync(): Deferred<Boolean> {
        return viewModelScope.async {
            delay(2000)
            if (prefs.prefUserToken.isNullOrEmpty()) {
                navigator?.goToLogin()
            } else {
                navigator?.goToStory()
            }

            return@async true
        }
    }
}