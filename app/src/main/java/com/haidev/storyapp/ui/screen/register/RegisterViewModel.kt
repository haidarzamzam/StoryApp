package com.haidev.storyapp.ui.screen.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haidev.storyapp.data.model.RegisterModel
import com.haidev.storyapp.data.model.Resource
import com.haidev.storyapp.data.source.repository.StoryRepository
import com.haidev.storyapp.ui.base.BaseViewModel
import com.haidev.storyapp.util.ErrorUtil
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: StoryRepository, application: Application) :
    BaseViewModel<RegisterNavigator>(application) {
    private val _responseRegister = MutableLiveData<Resource<RegisterModel.Response>>()
    val responseRegister: MutableLiveData<Resource<RegisterModel.Response>>
        get() = _responseRegister

    fun postRegister(payload: RegisterModel.Payload) {
        viewModelScope.launch {
            _responseRegister.postValue(Resource.loading(null))
            try {
                val response = repository.postRegister(payload)
                _responseRegister.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _responseRegister.postValue(
                    Resource.error(
                        ErrorUtil.getErrorThrowableMsg(t),
                        null,
                        t
                    )
                )
            }
        }
    }
}