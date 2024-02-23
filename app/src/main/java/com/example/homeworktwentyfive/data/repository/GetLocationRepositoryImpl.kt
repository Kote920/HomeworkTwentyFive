package com.example.homeworktwentyfive.data.repository

import android.annotation.SuppressLint
import android.util.Log.d
import com.example.homeworktewentytwo.data.common.Resource
import com.example.homeworktwentyfive.domain.repository.GetLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLocationRepositoryImpl @Inject constructor(private val fusedLocationProviderClient: FusedLocationProviderClient) :
    GetLocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Flow<Resource<LatLng>> = withContext(Dispatchers.IO){

        flow {
            try {
                emit(Resource.Loading())
                val location = fusedLocationProviderClient.lastLocation.await()
                emit(Resource.Success(LatLng(location.latitude, location.longitude)))

            }catch (e: Exception){
                emit(Resource.Failed("Error"))
                d("errorinrep", e.toString())
            }


         }

    }
}