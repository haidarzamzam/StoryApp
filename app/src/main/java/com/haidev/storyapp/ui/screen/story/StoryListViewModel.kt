package com.haidev.storyapp.ui.screen.story

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class StoryListViewModel(private val repository: StoryRepository, application: Application) :
    BaseViewModel<StoryListNavigator>(application) {

    val responseStory: LiveData<PagingData<StoryModel.Response.Story>>
        get() = repository.getStory().cachedIn(viewModelScope)

    fun getStories() {
        viewModelScope.launch {
            try {
                repository.getStory().cachedIn(viewModelScope)
            } catch (t: Throwable) {

            }
        }
    }
}