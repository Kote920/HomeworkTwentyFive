package com.example.homeworktwentyfive.di

import android.content.Context
import com.example.homeworktwentyfive.data.repository.GetLocationRepositoryImpl
import com.example.homeworktwentyfive.domain.repository.GetLocationRepository
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @Singleton
//    fun provideGetLocationRepository(
//        fusedLocationProviderClient: FusedLocationProviderClient
//    ): GetLocationRepository {
//        return GetLocationRepositoryImpl(fusedLocationProviderClient)
//    }
//}