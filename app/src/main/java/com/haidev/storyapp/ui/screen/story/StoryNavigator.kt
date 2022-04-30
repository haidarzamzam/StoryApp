package com.haidev.storyapp.ui.screen.story

import com.haidev.storyapp.data.model.StoryModel

interface StoryNavigator {
    fun goToLogin()
    fun goToDetailStory(data: StoryModel.Response.Story)
    fun goToAddStory()
}