package de.challenge.api.model

sealed class ApiResult<out T> {
    abstract fun isSuccess(): Boolean

    data class Success<out T>(val result: T) : ApiResult<T>() {
        override fun isSuccess(): Boolean = true
    }

    data class Failure<out T>(val error: Throwable) : ApiResult<T>() {
        override fun isSuccess(): Boolean = false
    }
}