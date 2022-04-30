package com.haidev.storyapp.data.model

object LoginModel {
    data class Response(
        val error: Boolean,
        val loginResult: LoginResult,
        val message: String
    ) {
        data class LoginResult(
            val name: String,
            val token: String,
            val userId: String
        )
    }

    data class Payload(
        val email: String,
        val password: String
    )
}