package de.challenge.api.network

import de.challenge.api.mappers.ResponseMapper
import de.challenge.api.model.ApiResult
import de.challenge.api.model.NetworkingError
import de.challenge.api.model.VehicleListResponseData
import java.net.UnknownHostException

interface VehicleService {
    suspend fun getVehicles(): ApiResult<VehicleListResponseData>
}

class VehicleServiceImpl(
    private val vehicleApi: VehicleApi,
    private val responseMapper: ResponseMapper
) : VehicleService {
    override suspend fun getVehicles(): ApiResult<VehicleListResponseData> =
        try {
            with(responseMapper) { vehicleApi.getVehicles().mapResult() }
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                ApiResult.Failure(NetworkingError.NetworkError(e))
            } else {
                ApiResult.Failure(e)
            }
        }
}