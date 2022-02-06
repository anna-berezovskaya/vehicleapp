package de.challenge.tiermobility.ui

import de.challenge.tiermobility.R

sealed class ViewError(message : Int) : Throwable(){
    object NoInternet : ViewError(R.string.no_internet)
    object NoLocationPermission : ViewError(R.string.no_location_permission)
    object ServerError : ViewError(R.string.server_error)
}