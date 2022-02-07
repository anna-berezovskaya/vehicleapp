package de.challenge.tiermobility.features.vehicle.model

sealed class ViewError : Throwable() {
    object SourceError : ViewError()
    object NoInternet : ViewError()
}