package de.challenge.tiermobility.mappers

import de.challenge.api.model.ApiResult
import de.challenge.api.model.NetworkingError
import de.challenge.tiermobility.ui.ViewError

interface Mapper<I, O> {
    fun I.map(): O
}

object ErrorMapper : Mapper<NetworkingError, ViewError> {
    override fun NetworkingError.map(): ViewError = when (this) {
        is NetworkingError.ServerError -> ViewError.ServerError
        is NetworkingError.Unknown -> ViewError.ServerError
        is NetworkingError.NetworkError -> ViewError.NoInternet
    }
}

/**
 * generic error handling also could be added here
 * and class could be moved to different module.
 * Leaving it like this cause of time limitations of coding challenge
 */
class NetworkingResultMapper<I, O>(private val successMapper: Mapper<I, O>) :
    Mapper<ApiResult<I>, ApiResult<O>> {
    override fun ApiResult<I>.map(): ApiResult<O> = when (this) {
        is ApiResult.Success -> {
            val result = with(successMapper) { this@map.result.map() }
            ApiResult.Success(result)
        }
        is ApiResult.Failure -> {
            val error = this@map.error
            val resultError =
                if (error is NetworkingError) with(ErrorMapper) { error.map() } else error
            ApiResult.Failure(resultError)
        }
    }
}
