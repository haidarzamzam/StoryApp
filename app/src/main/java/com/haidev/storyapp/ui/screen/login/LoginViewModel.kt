package com.haidev.storyapp.ui.screen.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haidev.storyapp.data.model.LoginModel
import com.haidev.storyapp.data.model.Resource
import com.haidev.storyapp.data.source.repository.UserRepository
import com.haidev.storyapp.ui.base.BaseViewModel
import com.haidev.storyapp.util.ErrorUtil
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository, application: Application) :
    BaseViewModel<LoginNavigator>(application) {
    private val _responseLogin = MutableLiveData<Resource<LoginModel.Response>>()
    val responseLogin: MutableLiveData<Resource<LoginModel.Response>>
        get() = _responseLogin

    fun postLogin(payload: LoginModel.Payload) {
        _responseLogin.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.postLogin(payload)
                _responseLogin.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _responseLogin.postValue(
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