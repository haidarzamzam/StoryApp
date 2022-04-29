package com.haidev.storyapp.ui.base

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(application: Application) : AndroidViewModel(application) {
    var toolbarState: MutableLiveData<Boolean> = MutableLiveData(false)
    private var mNavigator: WeakReference<N>? = null
    private val mCompositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }

    var compositeDisposable = mCompositeDisposable

    var navigator: N?
        get() {
            return mNavigator?.get()
        }
        set(value) {
            mNavigator = WeakReference<N>(value)
        }
}