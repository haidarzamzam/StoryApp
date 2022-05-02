package com.haidev.storyapp.di

import com.haidev.storyapp.data.source.local.sharepref.PrefManager
import org.koin.core.context.GlobalContext

val prefs by lazy { GlobalContext.get().get<PrefManager>() }