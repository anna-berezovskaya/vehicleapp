package de.challenge.tiermobility.finding.ui

import android.location.Location
import de.challenge.tiermobility.features.vehicle.model.Vehicle
import de.challenge.tiermobility.features.vehicle.model.VehicleMarker
import de.challenge.tiermobility.features.vehicle.repository.VehicleMarkersRepo
import de.challenge.tiermobility.features.vehicle.ui.VehicleMapViewModel
import de.challenge.tiermobility.features.vehicle.utils.DistanceUseCase
import de.challenge.tiermobility.location.repository.LocationRepository
import de.challenge.tiermobility.ui.UiState
import de.challenge.tiermobility.utils.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VehicleMapViewModelTest {

    private val successResults:
            UiState.Data<List<VehicleMarker>> =
        UiState.Data(
            listOf(
                VehicleMarker(
                    "id1",
                    1.0,
                    1.0,
                    Vehicle("vehicleId1", "zone1", 1, "resolution1", "model1")
                ),
                VehicleMarker(
                    "id2",
                    2.0,
                    2.0,
                    Vehicle("vehicleId2", "zone2", 2, "resolution2", "model2")
                )
            )
        )


    private var getVehicleMarkersUseCaseMock: VehicleMarkersRepo = mockk()
    private var locationRepositoryMock: LocationRepository = mockk()
    private var distanceUseCase: DistanceUseCase = mockk()

    private val vehicleMapViewModel = VehicleMapViewModel(
        getVehicleMarkersUseCaseMock,
        locationRepositoryMock,
        distanceUseCase
    )

    private val stateList = mutableListOf<UiState<List<VehicleMarker>>>()

    @get:Rule
    val rule = CoroutineTestRule()

    @Before
    fun setUp() {
        every { distanceUseCase.getDistance(any(), any()) } returns 0f
        stateList.clear()
        runTest {
            vehicleMapViewModel.uiState
                .onEach(stateList::add)
                .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        }
    }

    @Test
    fun shouldReturnResultsIfLocationIsSet() = runTest {
        coEvery { getVehicleMarkersUseCaseMock.getVehicles() } returns (successResults)
        coEvery { locationRepositoryMock.requestLastLocation() } returns Location("")

        vehicleMapViewModel.locationPermissionGranted = true

        assert(stateList[0] is UiState.Loading)
        assert(stateList.contains(successResults))
    }

    @Test
    fun shouldReturnResultsIfLocationIsNull() = runTest {
        coEvery { getVehicleMarkersUseCaseMock.getVehicles() } returns (successResults)
        coEvery { locationRepositoryMock.requestLastLocation() } returns null

        vehicleMapViewModel.locationPermissionGranted = true

        assert(stateList[0] is UiState.Loading)
        assert(stateList.contains(successResults))
    }

    @Test
    fun shouldReturnErrorIfLocationIsRevoked() = runTest {
        coEvery { getVehicleMarkersUseCaseMock.getVehicles() } returns (successResults)
        coEvery { locationRepositoryMock.requestLastLocation() } returns null

        vehicleMapViewModel.locationPermissionGranted = false

        assert(stateList.any { it is UiState.NoLocationPermission })
        assertFalse(stateList.contains(successResults))
    }


    @Test
    fun shouldReturnErrorIfDataSourceReturnError() = runTest {
        coEvery { getVehicleMarkersUseCaseMock.getVehicles() } returns (UiState.ServerError())
        coEvery { locationRepositoryMock.requestLastLocation() } returns null

        vehicleMapViewModel.locationPermissionGranted = true

        assert(stateList.any { it is UiState.Loading })
        assert(stateList.any { it is UiState.ServerError })
        assertFalse(stateList.contains(successResults))
    }

    @Test
    fun shouldReturnNetworkingErrorIfNoInternet() = runTest {
        coEvery { getVehicleMarkersUseCaseMock.getVehicles() } returns
                (UiState.NoInternet())
        coEvery { locationRepositoryMock.requestLastLocation() } returns null

        vehicleMapViewModel.locationPermissionGranted = true

        assert(stateList.any { it is UiState.Loading })
        assert(stateList.any { it is UiState.NoInternet })
        assertFalse(stateList.any { it is UiState.ServerError })
        assertFalse(stateList.contains(successResults))
    }
}