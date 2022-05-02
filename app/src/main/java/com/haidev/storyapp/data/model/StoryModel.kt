package com.haidev.storyapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

object StoryModel {
    @Parcelize
    data class Response(
        val error: Boolean,
        val listStory: List<Story>,
        val message: String
    ) : Parcelable {
        @Parcelize
        @Entity(tableName = "story")
        data class Story(
            @PrimaryKey
            val id: String,
            val createdAt: String?,
            val description: String?,
            val lat: Double?,
            val lon: Double?,
            val name: String?,
            val photoUrl: String?
        ) : Parcelable
    }
}