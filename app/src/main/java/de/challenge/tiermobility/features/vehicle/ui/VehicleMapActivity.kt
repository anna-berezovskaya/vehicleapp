package de.challenge.tiermobility.features.vehicle.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import de.challenge.tiermobility.R
import de.challenge.tiermobility.databinding.ActivityMapsBinding
import de.challenge.tiermobility.features.vehicle.model.Vehicle
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.ui.UiState
import de.challenge.tiermobility.utils.PermissionUtils


@AndroidEntryPoint
class VehicleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: VehicleMapViewModel by viewModels()

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForLocationPermission()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        checkForPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateUi(it)
            }
        }
    }

    private fun checkForPermission() {
        if (PermissionUtils.hasLocationPermission(applicationContext)) {
            viewModel.locationPermissionGranted = true
        } else if (!PermissionUtils.hasLocationPermission(applicationContext))
            requestLocationPermission()
    }

    private fun registerForLocationPermission() {
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) || permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            viewModel.locationPermissionGranted = granted
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun updateMap(data: List<VehicleMarker>?) {
        data?.forEachIndexed { index, vehicleMarker ->
            val position = LatLng(vehicleMarker.latitude, vehicleMarker.longitude)
            val marker =
                map.addMarker(MarkerOptions().position(position).title(vehicleMarker.vehicle.model))
            marker?.tag = index
            marker?.let { markers.add(marker) }
            markers.getOrNull(0)?.let {
                it.showInfoWindow()
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 15f))
            }
            map.setOnMarkerClickListener { marker ->
                val index = marker.tag as? Int
                index?.let { updateBottomSheet(viewModel.vehicleMarkers?.getOrNull(it)?.vehicle) }
                false
            }
        } ?: map.clear()
    }

    private fun updateUi(uiState: UiState<List<VehicleMarker>>) {
        when (uiState) {
            is UiState.Data -> {
                binding.bottomSheet.loadingProgress.visibility = View.GONE
                binding.bottomSheet.error.visibility = View.GONE
                binding.bottomSheet.retry.visibility = View.GONE
                updateMap(uiState.data)
                updateBottomSheet(uiState.data[0].vehicle)
            }

            is UiState.NoLocationPermission -> {
                updateBottomSheet(null)
                binding.bottomSheet.loadingProgress.visibility = View.GONE
                binding.bottomSheet.error.visibility = View.VISIBLE
                binding.bottomSheet.retry.setOnClickListener(null)
                binding.bottomSheet.retry.visibility = View.GONE
                binding.bottomSheet.error.text = getString(R.string.no_location_permission)
            }

            is UiState.ServerError -> {
                updateBottomSheet(null)
                binding.bottomSheet.loadingProgress.visibility = View.GONE
                binding.bottomSheet.error.visibility = View.VISIBLE
                binding.bottomSheet.retry.visibility = View.VISIBLE
                binding.bottomSheet.retry.setOnClickListener {
                    viewModel.loadData()
                }
                binding.bottomSheet.error.text = getString(R.string.server_error)
                binding.bottomSheet.retry.text = getString(R.string.retry)
            }
            is UiState.NoInternet -> {
                binding.bottomSheet.loadingProgress.visibility = View.GONE
                binding.bottomSheet.error.visibility = View.VISIBLE
                binding.bottomSheet.retry.visibility = View.VISIBLE
                binding.bottomSheet.retry.setOnClickListener {
                    viewModel.loadData()
                }
                binding.bottomSheet.error.text = getString(R.string.no_internet)
                binding.bottomSheet.retry.text = getString(R.string.retry)
            }

            is UiState.Loading -> {
                updateBottomSheet(null)
                binding.bottomSheet.loadingProgress.visibility = View.VISIBLE
                binding.bottomSheet.error.visibility = View.GONE
                binding.bottomSheet.retry.visibility = View.GONE
            }
        }
    }

    private fun updateBottomSheet(data: Vehicle?) {

        if (data?.battery == null) {
            binding.bottomSheet.battery.visibility = View.GONE
        } else {
            binding.bottomSheet.battery.visibility = View.VISIBLE
            binding.bottomSheet.battery.text = data.battery.toString()
        }

        if (data?.model == null) {
            binding.bottomSheet.model.visibility = View.GONE
        } else {
            binding.bottomSheet.model.visibility = View.VISIBLE
            binding.bottomSheet.model.text = data.model
        }

        if (data?.resolution == null) {
            binding.bottomSheet.status.visibility = View.GONE
        } else {
            binding.bottomSheet.status.visibility = View.VISIBLE
            binding.bottomSheet.status.text = data.resolution
        }
    }
}