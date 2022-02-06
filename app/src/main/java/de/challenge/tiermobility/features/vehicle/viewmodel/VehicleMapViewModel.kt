package de.challenge.tiermobility.features.vehicle.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.challenge.api.model.ApiResult
import de.challenge.tiermobility.features.vehicle.domain.GetVehicleMarkersUseCase
import de.challenge.tiermobility.features.vehicle.domain.VehicleMarkerSortHelper
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.ui.UiState
import de.challenge.tiermobility.ui.ViewError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleMapViewModel @Inject constructor(
    private val getVehicleMarkersUseCase: GetVehicleMarkersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<VehicleMarker>>>(UiState.Loading())
    val uiState: StateFlow<UiState<List<VehicleMarker>>> = _uiState

    private var locationPermissionGranted = false
    private var location: Location? = null
    var vehicleMarkers : List<VehicleMarker>? = null
    private set

    fun updateLocationPermission(isGranted: Boolean) {
        locationPermissionGranted = isGranted
        if (!locationPermissionGranted) {
            _uiState.value = UiState.Error(ViewError.NoLocationPermission)
        }
    }

    fun updateLocation(location: Location?) {
        this.location = location
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading())
            if (!locationPermissionGranted) {
                _uiState.emit(UiState.Error(ViewError.NoLocationPermission))
                return@launch
            }
            when (val result = getVehicleMarkersUseCase.run()) {
                is ApiResult.Failure -> {
                    when (val error = result.error) {
                        is ViewError -> _uiState.emit(UiState.Error(error))
                        else -> _uiState.emit(UiState.Error(ViewError.ServerError))
                    }
                }
                is ApiResult.Success -> {
                    val list = result.result
                    val sortedList =
                        location?.let { VehicleMarkerSortHelper.sortByDistance(list, it) } ?: list
                    vehicleMarkers = sortedList
                    _uiState.emit(UiState.Data(sortedList))
                }
            }
        }
    }
}
