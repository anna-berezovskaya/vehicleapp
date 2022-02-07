package de.challenge.tiermobility.features.vehicle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import de.challenge.api.model.ApiResult
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.features.vehicle.model.ViewError
import de.challenge.tiermobility.features.vehicle.repository.VehicleMarkersRepo
import de.challenge.tiermobility.location.repository.LocationRepository
import de.challenge.tiermobility.ui.UiState
import de.challenge.tiermobility.ui.toLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleMapViewModel @Inject constructor(
    private val vehiclesRepo: VehicleMarkersRepo,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<VehicleMarker>>>(UiState.Loading())
    val uiState: StateFlow<UiState<List<VehicleMarker>>> = _uiState

    private val location = locationRepository.location

    init {
        /**
         * data is only loaded on very start or when location permission
         * is set/changed
         */
        loadData()
        viewModelScope.launch { location.collect{
            loadData()
        } }
    }

    // private var location: LocationStatus? = null
    var vehicleMarkers: List<VehicleMarker>? = null
        private set
    var locationPermissionGranted = false
        set(value) {
            if (value) {
                val oldValue = field
                field = value
                if (!oldValue){
                    loadData()
                }
                locationRepository.requestLastLocation()
            }
        }

//    /**
//     * if first run or first permisson update, then load data
//     * if permission revoked show error
//     * if location updated the sort the list
//     */
//    fun updateLocationStatus(locationStatus: LocationStatus) {
//        location = locationStatus
//        if (!locationStatus.locationGranted) {
//            _uiState.value = UiState.NoLocationPermission()
//        } else {
//            loadData()
//        }
//    }

    fun loadData() {
        viewModelScope.launch {
            if (vehicleMarkers == null) {
                _uiState.emit(UiState.Loading())
                if (!locationPermissionGranted) {
                    _uiState.emit(UiState.NoLocationPermission())
                    return@launch
                }
                when (val result = vehiclesRepo.getVehicles()) {
                    is ApiResult.Failure -> {
                        when (result.error) {
                            is ViewError.NoInternet -> _uiState.emit(UiState.NoInternet())
                            else -> _uiState.emit(UiState.ServerError())
                        }
                    }
                    is ApiResult.Success -> {
                        vehicleMarkers = result.result
                        updateDataWithLocation()
                    }
                }
            } else {
                updateDataWithLocation()
            }
        }
    }

    private fun updateDataWithLocation() {
        viewModelScope.launch {
            if (location.value != null) {
                vehicleMarkers = vehicleMarkers?.sortedBy {
                    LatLng(it.latitude, it.longitude).toLocation().distanceTo(location.value)
                }
            }
            vehicleMarkers?.let { _uiState.emit(UiState.Data(it)) } ?: _uiState.emit(
                UiState.ServerError()
            )
        }
    }
}
