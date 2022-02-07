package de.challenge.api.mappers

import de.challenge.api.model.ApiResult
import de.challenge.api.model.NetworkingError
import retrofit2.Response
import java.io.IOException

interface ResponseMapper  {
    fun <T> Response<T>.mapResult(): ApiResult<T>
}

class NetworkResponseMapper : ResponseMapper {

    private fun <T> Response<T>.emptyError(): NetworkingError =
        NetworkingError.ServerError("empty response", this.code(), this.headers())

    override fun <T> Response<T>.mapResult(): ApiResult<T> = try {
        if (this.isSuccessful) {
            val body = this.body()
            body?.let { ApiResult.Success(it) } ?: ApiResult.Failure(this.emptyError())

        } else {
            val errorBody = this.errorBody()?.string()
            errorBody?.let {
                ApiResult.Failure(
                    NetworkingError.ServerError(
                        it,
                        this.code(),
                        this.headers()
                    )
                )
            } ?: ApiResult.Failure(this.emptyError())

        }
    } catch (e: Throwable) {
        when (e) {
            is IOException -> ApiResult.Failure(NetworkingError.NetworkError(e))
            else -> ApiResult.Failure(NetworkingError.Unknown(e))
        }
    }
}
