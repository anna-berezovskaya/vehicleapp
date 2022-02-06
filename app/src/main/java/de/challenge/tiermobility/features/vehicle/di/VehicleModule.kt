package de.challenge.tiermobility.features.vehicle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.challenge.api.network.ResponseMapper
import de.challenge.api.network.VehicleApi
import de.challenge.api.network.VehicleService
import de.challenge.api.network.VehicleServiceImpl
import de.challenge.tiermobility.features.vehicle.usecase.GetVehicleMarkersUseCase
import de.challenge.tiermobility.features.vehicle.model.VehicleListMapper
import de.challenge.tiermobility.features.vehicle.model.VehicleListResponseDataMapper
import de.challenge.tiermobility.mappers.NetworkingResultMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VehicleModule {
    @Provides
    fun provideGetVehicleMarkersUseCase(service: VehicleService): GetVehicleMarkersUseCase =
        GetVehicleMarkersUseCase(service, getVehicleResponseMapper())


    private fun getVehicleResponseMapper() = NetworkingResultMapper(VehicleListResponseDataMapper)

    @Provides
    @Singleton
    fun bindVehicleService(
        responseMapper: ResponseMapper,
        vehicleApi: VehicleApi
    ): VehicleService =
        VehicleServiceImpl(vehicleApi, responseMapper)
}