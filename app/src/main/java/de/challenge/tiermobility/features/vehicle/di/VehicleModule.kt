package de.challenge.tiermobility.features.vehicle.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.challenge.api.model.NetworkingError
import de.challenge.api.model.VehicleListResponseData
import de.challenge.api.mappers.ResponseMapper
import de.challenge.api.network.VehicleApi
import de.challenge.api.network.VehicleService
import de.challenge.api.network.VehicleServiceImpl
import de.challenge.tiermobility.features.vehicle.model.*
import de.challenge.tiermobility.features.vehicle.repository.DefaultVehicleMarkersRepo
import de.challenge.tiermobility.features.vehicle.repository.VehicleMarkersRepo
import de.challenge.tiermobility.features.vehicle.utils.DefaultDistanceUseCase
import de.challenge.tiermobility.features.vehicle.utils.DistanceUseCase
import de.challenge.tiermobility.location.repository.GoogleLocationRepository
import de.challenge.tiermobility.location.repository.LocationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VehicleModule {

    @Provides
    @Singleton
    fun provideVehicleMarkerRepository(
        vehicleService: VehicleService,
        successMapper : Mapper<VehicleListResponseData, List<VehicleMarker>>,
    ) : VehicleMarkersRepo = DefaultVehicleMarkersRepo(vehicleService, successMapper)

    @Provides
    @Singleton
    fun provideVehicleListResponseDataMapper() : Mapper<VehicleListResponseData, List<VehicleMarker>> = VehicleListResponseDataMapper

    @Provides
    @Singleton
    fun provideDistanceUseCase() : DistanceUseCase = DefaultDistanceUseCase()

    @Provides
    @Singleton
    fun locationRepository(@ApplicationContext context : Context) : LocationRepository = GoogleLocationRepository(context.applicationContext)

    @Provides
    @Singleton
    fun bindVehicleService(
        responseMapper: ResponseMapper,
        vehicleApi: VehicleApi
    ): VehicleService =
        VehicleServiceImpl(vehicleApi, responseMapper)
}