package de.challenge.tiermobility.features.vehicle.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import de.challenge.tiermobility.ui.toLocation

class DefaultDistanceUseCase : DistanceUseCase {
    override fun getDistance(latLng: LatLng, location: Location) : Float =
        LatLng(latLng.latitude, latLng.longitude).toLocation().distanceTo(location)

}

interface DistanceUseCase {
    fun getDistance(latLng: LatLng, location: Location) : Float
}