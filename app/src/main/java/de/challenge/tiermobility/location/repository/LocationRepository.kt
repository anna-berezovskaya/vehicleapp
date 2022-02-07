package de.challenge.tiermobility.location.repository

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import de.challenge.tiermobility.utils.CoroutineUtils

class GoogleLocationRepository(@ApplicationContext context: Context) :
    LocationRepository {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    @Throws(SecurityException::class)
    override suspend fun requestLastLocation(): Location? = CoroutineUtils.awaitCallback {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    it.onComplete(location)
                }
                .addOnCanceledListener {
                    it.onComplete(null)
                }
        } catch (e: SecurityException) {
            it.onComplete(null)
        }
    }
}

interface LocationRepository {
    suspend fun requestLastLocation(): Location?
}