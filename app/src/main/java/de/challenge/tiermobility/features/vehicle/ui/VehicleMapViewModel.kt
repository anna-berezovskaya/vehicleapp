package de.challenge.tiermobility.features.vehicle.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.features.vehicle.repository.VehicleMarkersRepo
import de.challenge.tiermobility.features.vehicle.utils.DistanceUseCase
import de.challenge.tiermobility.location.repository.LocationRepository
import de.challenge.tiermobility.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleMapViewModel @Inject constructor(
    private val vehiclesRepo: VehicleMarkersRepo,
    private val locationRepository: LocationRepository,
    private val distanceUseCase: DistanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<VehicleMarker>>>(UiState.Loading())
    val uiState: StateFlow<UiState<List<VehicleMarker>>> = _uiState

    var vehicleMarkers: List<VehicleMarker>? = null
        private set

    var location: Location? = null
    var locationPermissionGranted = false
        set(value) {
            val oldValue = field
            field = value
            if (field) {
                requestLocation()
            } else {
                _uiState.value = UiState.NoLocationPermission()
            }
        }


    private fun requestLocation() {
        viewModelScope.launch {
            location = locationRepository.requestLastLocation()
            loadData()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            if (vehicleMarkers == null) {
                if (!locationPermissionGranted) {
                    _uiState.emit(UiState.NoLocationPermission())
                    return@launch
                }
                _uiState.emit(UiState.Loading())

                val result = vehiclesRepo.getVehicles()
                _uiState.emit(result)

                if (result is UiState.Data) {
                    vehicleMarkers = result.data
                    updateDataWithLocation()
                }

            } else {
                updateDataWithLocation()
            }
        }
    }

    private fun updateDataWithLocation() {
        viewModelScope.launch {
            val l = location
            if (l != null) {
                vehicleMarkers = vehicleMarkers?.sortedBy {
                    distanceUseCase.getDistance(LatLng(it.latitude, it.longitude), l)
                }
            }
            vehicleMarkers?.let { _uiState.emit(UiState.Data(it)) } ?: _uiState.emit(
                UiState.ServerError()
            )
        }
    }
}
