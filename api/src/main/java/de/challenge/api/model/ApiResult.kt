package de.challenge.api.model

sealed class ApiResult<out T> {
    abstract fun isSuccess(): Boolean

    data class Success<out T>(val result: T) : ApiResult<T>() {
        override fun isSuccess(): Boolean = true
    }

    data class Failure<out T>(val error: Throwable) : ApiResult<T>() {
        override fun isSuccess(): Boolean = false
    }

    fun <O> map(mapFunction: (T) -> O): ApiResult<O> =
        when (this) {
            is Failure -> Failure(this.error)
            is Success -> Success(mapFunction(this.result))
        }

    fun <O : Throwable> mapError(mapFunction: (Throwable) -> O): ApiResult<T> =
        when (this) {
            is Failure -> Failure(mapFunction(this.error))
            is Success -> this
        }

}