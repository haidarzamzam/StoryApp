package com.haidev.storyapp.data.source.sharepref

import android.content.Context
import com.haidev.storyapp.MyApp

class PrefManager(val context: Context = MyApp.applicationContext()) {
    private val contextMode = Context.MODE_PRIVATE
    private var pref = context.getSharedPreferences("STORY_PREFS", contextMode)
    private var editor = pref.edit()

    var prefUserToken: String?
        get() {
            return pref.getString("USER_TOKEN", null)
        }
        set(value) {
            if (value != null) {
                editor.putString("USER_TOKEN", value)?.apply()
            }
        }
}