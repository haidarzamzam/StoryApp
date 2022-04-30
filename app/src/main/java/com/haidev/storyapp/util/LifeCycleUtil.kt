package com.haidev.storyapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData

fun <T> AppCompatActivity.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { action.invoke(it) })
}