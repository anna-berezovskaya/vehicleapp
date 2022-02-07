package de.challenge.tiermobility.ui

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun LatLng.toLocation() = Location("").apply {
    latitude = latitude
    longitude = longitude
}