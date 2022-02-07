package de.challenge.tiermobility.features.vehicle.repository

import de.challenge.api.model.ApiResult
import de.challenge.api.model.NetworkingError
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.network.VehicleService
import de.challenge.tiermobility.features.vehicle.model.Mapper
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.ui.UiState
import javax.inject.Inject

class DefaultVehicleMarkersRepo @Inject constructor(
    private val service: VehicleService,
    private val mapper: Mapper<VehicleListResponseData, List<VehicleMarker>>,
) : VehicleMarkersRepo {

    override suspend fun getVehicles(): UiState<List<VehicleMarker>> =
        when (val response = service.getVehicles().map { mapper.map(it) }) {
            is ApiResult.Success -> UiState.Data(response.result)
            is ApiResult.Failure -> {
                when (response.error) {
                    is NetworkingError.NetworkError -> UiState.NoInternet()
                    else -> UiState.ServerError()
                }
            }

        }

}

interface VehicleMarkersRepo {
    suspend fun getVehicles(): UiState<List<VehicleMarker>>
}