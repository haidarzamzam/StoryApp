package com.haidev.storyapp.ui.screen.story

import android.app.Application
import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.ui.base.BaseViewModel

class StoryViewModel(private val repository: StoryRepository, application: Application) :
    BaseViewModel<StoryNavigator>(application)