package com.example.homeworktwentyfive.domain.repository

import com.example.homeworktewentytwo.data.common.Resource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface GetLocationRepository {

    suspend fun getCurrentLocation(): Flow<Resource<LatLng>>
}