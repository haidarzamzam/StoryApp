package com.haidev.storyapp.util

open class ContextProviders {
    companion object {
        @Volatile
        private var INSTANCE: ContextProviders? = null

        fun getInstance(): ContextProviders {
            return INSTANCE ?: synchronized(this) {
                ContextProviders()
            }.also {
                INSTANCE = it
            }
        }
    }
}