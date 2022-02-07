package de.challenge.tiermobility.features.vehicle.repository

import de.challenge.api.model.ApiResult
import de.challenge.api.model.NetworkingError
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.network.VehicleService
import de.challenge.tiermobility.features.vehicle.model.Mapper
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.features.vehicle.model.ViewError
import javax.inject.Inject

class DefaultVehicleMarkersRepo @Inject constructor(
    private val service: VehicleService,
    private val mapper: Mapper<VehicleListResponseData, List<VehicleMarker>>,
    private val errorMapper: Mapper<NetworkingError, ViewError>
) : VehicleMarkersRepo {

    override suspend fun getVehicles(): ApiResult<List<VehicleMarker>> =
        service.getVehicles().map { mapper.map(it) }

}

interface VehicleMarkersRepo {
    suspend fun getVehicles(): ApiResult<List<VehicleMarker>>
}