package com.moodi.someapp.data.repository

import com.moodi.someapp.util.Result
import com.moodi.someapp.data.mapper.mapToApp
import com.moodi.someapp.data.util.Resource
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.api.ApiClient
import com.moodi.someapp.domain.remote.dto.WeatherRequest
import kotlinx.coroutines.flow.flow


class WeatherRepositoryImpl(val apiClient: ApiClient) : WeatherRepository {

    override fun getWeather(lat: Double, lng: Double, unit: WeatherUnit) = flow {
        emit(Resource.Loading())

        val request = WeatherRequest(
            lat = lat, lng = lng, unit = unit.value
        )
        val result = apiClient.getWeather(request)
        if (result is Result.Success) {
            emit(
                Resource.Success(
                    result.data.mapToApp()
                )
            )
        } else if (result is Result.Failure) {
            emit(Resource.Error("Something Went Wrong"))
        } else {
            emit(Resource.Error("Network Problem"))
        }
    }
}