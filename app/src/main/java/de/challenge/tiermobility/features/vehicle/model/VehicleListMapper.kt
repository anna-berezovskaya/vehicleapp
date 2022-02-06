package de.challenge.tiermobility.features.vehicle.model

import de.challenge.api.model.VehicleListResponse
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.model.VehicleResponse
import de.challenge.tiermobility.features.vehicle.model.VehicleListMapper.map
import de.challenge.tiermobility.mappers.Mapper

/**
 * mappers transform values to view-readable format and
 * also doing basic sanity check, filtering our malformed or empty objects
 */
object VehicleListMapper : Mapper<VehicleListResponse, List<VehicleMarker>> {
    override fun VehicleListResponse.map(): List<VehicleMarker> {
        val list = mutableListOf<VehicleMarker>()
        this.current?.mapNotNullTo(list) { response -> with(VehicleMarkerMapper) { response.map() } }
        return list.toList()
    }

}

object VehicleListResponseDataMapper : Mapper<VehicleListResponseData, List<VehicleMarker>> {
    override fun VehicleListResponseData.map(): List<VehicleMarker> {
        return this.vehicleListResponse.map()
    }
}

object VehicleMarkerMapper : Mapper<VehicleResponse, VehicleMarker?> {
    override fun VehicleResponse.map(): VehicleMarker? {
        val vehicle = with(VehicleMapper) { map() }
        val id = this.id
        val longitude = this.longitude?.toDoubleOrNull()
        val latitude = this.latitude?.toDoubleOrNull()
        return if (id == null || vehicle == null || longitude == null || latitude == null) null else
            VehicleMarker(id, latitude, longitude, vehicle)
    }
}

object VehicleMapper : Mapper<VehicleResponse, Vehicle?> {
    override fun VehicleResponse.map(): Vehicle? {
        val id = this.id
        return if (id == null) null
        else Vehicle(id, this.zoneId, this.battery, this.resolution, this.model)
    }

}