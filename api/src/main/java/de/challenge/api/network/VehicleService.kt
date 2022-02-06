package de.challenge.api.network

import de.challenge.api.model.ApiResult
import de.challenge.api.model.VehicleListResponseData

interface VehicleService {
    suspend fun getVehicles(): ApiResult<VehicleListResponseData>
}

class VehicleServiceImpl(
    private val vehicleApi: VehicleApi,
    private val responseMapper: ResponseMapper
) : VehicleService {
    override suspend fun getVehicles(): ApiResult<VehicleListResponseData> =
        with(responseMapper) { vehicleApi.getVehicles().mapResult() }
}