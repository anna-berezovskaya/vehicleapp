package de.challenge.tiermobility.features.vehicle.model

data class VehicleMarker(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val vehicle: Vehicle
)