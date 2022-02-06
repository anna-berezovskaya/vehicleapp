package de.challenge.tiermobility.features.vehicle.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker

object VehicleMarkerSortHelper {
    fun sortByDistance(list: List<VehicleMarker>, location: Location): List<VehicleMarker> =
        list.sortedBy { LatLng(it.latitude, it.longitude).toLocation().distanceTo(location) }
}

fun LatLng.toLocation() = Location("").apply {
    latitude = latitude
    longitude = longitude
}