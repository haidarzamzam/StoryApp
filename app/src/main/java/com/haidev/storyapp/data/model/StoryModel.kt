package com.haidev.storyapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object StoryModel {
    @Parcelize
    data class Response(
        val error: Boolean,
        val listStory: List<Story>,
        val message: String
    ) : Parcelable {
        @Parcelize
        data class Story(
            val createdAt: String?,
            val description: String?,
            val id: String?,
            val lat: Double?,
            val lon: Double?,
            val name: String?,
            val photoUrl: String?
        ) : Parcelable
    }
}