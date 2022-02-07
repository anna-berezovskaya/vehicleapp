package de.challenge.tiermobility.location.repository

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoogleLocationRepository (@ApplicationContext context: Context) :
    LocationRepository {
    private val _location: MutableStateFlow<Location?> = MutableStateFlow(null)
    override val location: StateFlow<Location?> = _location
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    @Throws(SecurityException::class)
    override fun requestLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    _location.value = location
                }
                .addOnCanceledListener {
                    _location.value = null
                }
        } catch (e: SecurityException) {
        }
    }
}

interface LocationRepository {
    val location: StateFlow<Location?>
    fun requestLastLocation()
}