package de.challenge.tiermobility.features.vehicle.model

import de.challenge.api.model.NetworkingError
import de.challenge.api.model.VehicleListResponse
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.model.VehicleResponse
import de.challenge.tiermobility.features.vehicle.model.VehicleListMapper.map

interface Mapper<I, O> {
    fun map(i: I): O
}

/**
 * mappers transform values to view-readable format and
 * also doing basic sanity check, filtering our malformed or empty objects
 */
object VehicleListMapper : Mapper<VehicleListResponse, List<VehicleMarker>> {
    override fun map(response: VehicleListResponse): List<VehicleMarker> {
        val list = mutableListOf<VehicleMarker>()
        response.current?.mapNotNullTo(list) { resp -> VehicleMarkerMapper.map(resp) }
        return list.toList()
    }

}

object VehicleListResponseDataMapper : Mapper<VehicleListResponseData, List<VehicleMarker>> {
    override fun map(data: VehicleListResponseData): List<VehicleMarker> {
        return map(data.vehicleListResponse)
    }
}

object VehicleMarkerMapper : Mapper<VehicleResponse, VehicleMarker?> {
    override fun map(vehicleResponse: VehicleResponse): VehicleMarker? {
        val vehicle = VehicleMapper.map(vehicleResponse)
        val id = vehicleResponse.id
        val longitude = vehicleResponse.longitude?.toDoubleOrNull()
        val latitude = vehicleResponse.latitude?.toDoubleOrNull()
        return if (id == null || vehicle == null || longitude == null || latitude == null) null else
            VehicleMarker(id, latitude, longitude, vehicle)
    }
}

object VehicleMapper : Mapper<VehicleResponse, Vehicle?> {
    override fun map(i: VehicleResponse): Vehicle? {
        val id = i.id
        return if (id == null) null
        else Vehicle(id, i.zoneId, i.battery, i.resolution, i.model)
    }

}