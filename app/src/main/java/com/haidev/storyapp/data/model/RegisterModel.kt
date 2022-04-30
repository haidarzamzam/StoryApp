package com.haidev.storyapp.data.model

object RegisterModel {
    data class Response(
        val error: Boolean,
        val message: String
    )

    data class Payload(
        val email: String,
        val name: String,
        val password: String
    )
}
