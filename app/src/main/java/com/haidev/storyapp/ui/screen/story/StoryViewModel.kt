package com.haidev.storyapp.ui.screen.story

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haidev.storyapp.data.model.Resource
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.ui.base.BaseViewModel
import com.haidev.storyapp.util.ErrorUtil
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoryRepository, application: Application) :
    BaseViewModel<StoryNavigator>(application) {
    private val _responseStory = MutableLiveData<Resource<StoryModel.Response>>()
    val responseStory: MutableLiveData<Resource<StoryModel.Response>>
        get() = _responseStory

    fun getStory() {
        viewModelScope.launch {
            _responseStory.postValue(Resource.loading(null))
            try {
                val response = repository.getStory()
                _responseStory.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _responseStory.postValue(
                    Resource.error(
                        ErrorUtil.getServiceErrorMsg(t),
                        null,
                        t
                    )
                )
            }
        }
    }
}