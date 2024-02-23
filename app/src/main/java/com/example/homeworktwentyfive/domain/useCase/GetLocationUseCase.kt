package com.example.homeworktwentyfive.domain.useCase

import com.example.homeworktwentyfive.domain.repository.GetLocationRepository
import javax.inject.Inject


class GetLocationUseCase  @Inject constructor(private val repository: GetLocationRepository){

    suspend operator fun invoke() = repository.getCurrentLocation()
}