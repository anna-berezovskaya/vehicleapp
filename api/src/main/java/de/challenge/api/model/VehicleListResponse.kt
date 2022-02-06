package de.challenge.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleListResponse(
    @field:Json(name = "current") val current: List<VehicleResponse>?,
    @field:Json(name = "stats") val stats: VehiclesStats?
)

@JsonClass(generateAdapter = true)
data class VehicleListResponseData(@field:Json(name = "data") val vehicleListResponse: VehicleListResponse)

//"stats":{
//    "open":38,
//    "assigned":0,
//    "resolved":113
//}
@JsonClass(generateAdapter = true)
data class VehiclesStats(
    @field:Json(name = "open") val open: Int?,
    @field:Json(name = "assigned") val assigned: Int?,
    @field:Json(name = "resolved") val resolved: Int?
)

//"id":"b6cd8488-9112-434e-a353-2f6e72e9d921",
//"vehicleId":"35d3ccb0-7963-4808-9311-ded8f3edebd9",
//"hardwareId":"868446035952726",
//"zoneId":"BERLIN",
//"resolution":"CLAIMED",
//"resolvedBy":"KmOOdbjxxxWwnbFCLZXdtGLQPZ92",
//"resolvedAt":"2019-10-10T10:36:46.988Z",
//"battery":67,
//"state":"MAINTENANCE",
//"model":"AB",
//"fleetbirdId":118593,
//"latitude":52.537061,
//"longitude":13.216157
@JsonClass(generateAdapter = true)
data class VehicleResponse(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "vehicleId") val vehicleId: String?,
    @field:Json(name = "zoneId") val zoneId: String?,
    @field:Json(name = "resolution") val resolution: String?,
    @field:Json(name = "battery") val battery: Int?,
    @field:Json(name = "state") val state: String?,
    @field:Json(name = "model") val model: String?,
    @field:Json(name = "latitude") val latitude: String?,
    @field:Json(name = "longitude") val longitude: String?
)