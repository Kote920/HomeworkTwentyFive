package com.example.homeworktwentyfive.presentation.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.homeworktewentytwo.data.common.Resource
import com.example.homeworktwentyfive.domain.repository.GetLocationRepository
import com.example.homeworktwentyfive.domain.useCase.GetLocationUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val getLocationUseCase: GetLocationUseCase): ViewModel() {
    private val _locationFlow = MutableSharedFlow<Resource<LatLng>>()
    val locationFlow: SharedFlow<Resource<LatLng>> = _locationFlow.asSharedFlow()


    fun getLocation(){
        viewModelScope.launch {
            getLocationUseCase.invoke().collect{
                when (it) {
                    is Resource.Loading -> _locationFlow.emit(Resource.Loading())
                    is Resource.Success -> {
                        _locationFlow.emit(
                           Resource.Success(it.responseData))}
                    is Resource.Failed -> _locationFlow.emit(
                        Resource.Failed(it.message)
                    )
                }
            }

        }
    }




}