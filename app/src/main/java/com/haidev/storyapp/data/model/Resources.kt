package com.haidev.storyapp.data.model

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val throwable: Throwable?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String?, data: T? = null, err: Throwable? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg, err)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ResourcePaging<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val throwable: Throwable?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String?, data: T? = null, err: Throwable? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg, err)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}