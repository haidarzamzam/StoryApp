package com.haidev.storyapp.util

import com.google.gson.JsonParser
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {
    fun getServiceErrorMsg(error: Throwable): String {
        var message = "Have internal problem"
        message = when (error) {
            is HttpException -> {
                try {
                    val errorJsonString = error.response()?.errorBody()?.string()
                    JsonParser.parseString(errorJsonString)
                        .asJsonObject["message"]
                        .asString
                } catch (e: Exception) {
                    message
                }
            }
            is UnknownHostException -> "Unknown Error"
            is ConnectException -> "No internet connected"
            is SocketTimeoutException -> "No internet connected"
            is Errors.OfflineException -> "No internet connected"
            is Errors.FetchException -> "Fetch exception"
            else -> error.message ?: message
        }
        return message
    }
}

sealed class Errors
    (msg: String) : Exception(msg) {
    class OfflineException(msg: String = "Not Connected to Internet") : Errors(msg)
    class FetchException(msg: String) : Errors(msg)
}