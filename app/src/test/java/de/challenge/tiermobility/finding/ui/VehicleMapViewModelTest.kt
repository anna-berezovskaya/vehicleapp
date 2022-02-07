package de.challenge.tiermobility.finding.ui

import de.challenge.tiermobility.features.vehicle.domain.GetVehicleMarkersUseCase
import de.challenge.tiermobility.features.vehicle.viewmodel.VehicleMapViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class VehicleMapViewModelTest {

     @Mock lateinit var getVehicleMarkersUseCaseMock : GetVehicleMarkersUseCase
     val vehicleMapViewModel = VehicleMapViewModel(getVehicleMarkersUseCaseMock)

     @Before
     fun init(){
          MockitoAnnotations.openMocks(this)
     }

     @Test
     fun shouldReturnResultsIfLocationIsSet(){

     }

     @Test
     fun shouldReturnResultsIfLocationIsNull(){

     }

     @Test
     fun shouldReturnErrorIfLocationIsRevoked(){

     }

     @Test
     fun shouldReturnErrorIfDataSourceReturnError(){

     }

}