package com.haidev.storyapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.haidev.storyapp.di.apiRepositoryModule
import com.haidev.storyapp.di.remoteModule
import com.haidev.storyapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MyApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApp)
            modules(
                listOf(
                    viewModelModule,
                    apiRepositoryModule,
                    remoteModule
                )
            )
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}