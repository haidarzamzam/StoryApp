package com.haidev.storyapp.ui.screen.story

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haidev.storyapp.data.model.AddStoryModel
import com.haidev.storyapp.data.model.Resource
import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.ui.base.BaseViewModel
import com.haidev.storyapp.util.ErrorUtil
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository, application: Application) :
    BaseViewModel<AddStoryNavigator>(application) {

    private val _responseAddStory = MutableLiveData<Resource<AddStoryModel.Response>>()
    val responseAddStory: MutableLiveData<Resource<AddStoryModel.Response>>
        get() = _responseAddStory

    fun postAddStory(file: MultipartBody.Part, desc: RequestBody) {
        _responseAddStory.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.postStory(file, desc)
                _responseAddStory.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _responseAddStory.postValue(
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