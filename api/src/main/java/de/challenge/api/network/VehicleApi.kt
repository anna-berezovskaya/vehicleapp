package de.challenge.api.network

import de.challenge.api.model.VehicleListResponse
import de.challenge.api.model.VehicleListResponseData
import retrofit2.Response
import retrofit2.http.GET

interface VehicleApi {

    @GET("/b/5fa8ff8dbd01877eecdb898f")
    suspend fun getVehicles() : Response<VehicleListResponseData>
}