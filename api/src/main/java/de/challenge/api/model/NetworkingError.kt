package de.challenge.api.model

import okhttp3.Headers
import java.io.IOException

sealed class NetworkingError(message: String?, error: Throwable?) : Throwable() {

    data class ServerError(
        val body: String?, val code: Int?,
        val headers: Headers?
    ) : NetworkingError(body, null)

    data class NetworkError(
        val exception: IOException?
    ) : NetworkingError(null, exception)

    data class Unknown(
        val error: Throwable,
    ) : NetworkingError(null, error)
}