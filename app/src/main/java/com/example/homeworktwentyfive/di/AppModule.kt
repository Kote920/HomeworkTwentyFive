package com.example.homeworktwentyfive.di

import android.content.Context
import com.example.homeworktwentyfive.data.repository.GetLocationRepositoryImpl
import com.example.homeworktwentyfive.domain.repository.GetLocationRepository
import com.example.homeworktwentyfive.domain.useCase.GetLocationUseCase
import dagger.Module
import dagger.Provides
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideGetLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): GetLocationRepository {
        return GetLocationRepositoryImpl(fusedLocationProviderClient)
    }

    @Provides
    @Singleton
    fun provideGetLocationUseCase(
        getLocationRepository: GetLocationRepository
    ): GetLocationUseCase {
        return GetLocationUseCase(getLocationRepository)
    }
}