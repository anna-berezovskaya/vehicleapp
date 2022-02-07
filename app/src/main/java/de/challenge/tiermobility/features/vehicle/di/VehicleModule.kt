package de.challenge.tiermobility.features.vehicle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.challenge.api.model.NetworkingError
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.network.ResponseMapper
import de.challenge.api.network.VehicleApi
import de.challenge.api.network.VehicleService
import de.challenge.api.network.VehicleServiceImpl
import de.challenge.tiermobility.features.vehicle.model.*
import de.challenge.tiermobility.features.vehicle.repository.DefaultVehicleMarkersRepo
import de.challenge.tiermobility.features.vehicle.repository.VehicleMarkersRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VehicleModule {

    @Provides
    @Singleton
    fun provideVehicleMarkerRepository(
        vehicleService: VehicleService,
        successMapper : Mapper<VehicleListResponseData, List<VehicleMarker>>,
        errorMapper : Mapper<NetworkingError, ViewError>
    ) : VehicleMarkersRepo = DefaultVehicleMarkersRepo(vehicleService, successMapper, errorMapper)

    @Provides
    @Singleton
    fun vehicleListResponseDataMapper() : Mapper<VehicleListResponseData, List<VehicleMarker>> = VehicleListResponseDataMapper

    @Provides
    @Singleton
    fun errorMapper() : Mapper<NetworkingError, ViewError> = ErrorMapper

    @Provides
    @Singleton
    fun bindVehicleService(
        responseMapper: ResponseMapper,
        vehicleApi: VehicleApi
    ): VehicleService =
        VehicleServiceImpl(vehicleApi, responseMapper)
}