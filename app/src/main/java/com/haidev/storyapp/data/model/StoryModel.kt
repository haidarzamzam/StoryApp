package com.haidev.storyapp.data.model

object StoryModel {
    data class Response(
        val error: Boolean,
        val listStory: List<Story>,
        val message: String
    ) {
        data class Story(
            val createdAt: String,
            val description: String,
            val id: String,
            val lat: Double,
            val lon: Double,
            val name: String,
            val photoUrl: String
        )
    }
}