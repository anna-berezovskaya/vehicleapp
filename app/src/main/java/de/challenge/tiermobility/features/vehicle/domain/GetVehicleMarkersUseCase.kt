package de.challenge.tiermobility.features.vehicle.domain

import de.challenge.api.model.ApiResult
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.network.VehicleService
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.interfaces.UseCase
import de.challenge.tiermobility.mappers.NetworkingResultMapper

class GetVehicleMarkersUseCase(
    private val service: VehicleService,
    private val mapper: NetworkingResultMapper<VehicleListResponseData, List<VehicleMarker>>
) : UseCase<ApiResult<List<VehicleMarker>>> {
    override suspend fun run(): ApiResult<List<VehicleMarker>> =
        with(mapper) { service.getVehicles().map() }

}