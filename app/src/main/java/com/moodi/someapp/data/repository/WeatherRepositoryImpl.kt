package com.moodi.someapp.data.repository

import com.moodi.someapp.util.Result
import com.moodi.someapp.data.mapper.mapToApp
import com.moodi.someapp.data.util.Resource
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.core.location.AppLocation
import com.moodi.someapp.domain.remote.client.RemoteClient
import kotlinx.coroutines.flow.flow


class WeatherRepositoryImpl(val remoteClient: RemoteClient) : WeatherRepository {

    override fun getWeather(lat: Double, lng: Double) = flow {
        emit(Resource.Loading())

        val location = AppLocation(
            latitude = lat,
            longitude = lng,
        )
        val result = remoteClient.fetchWeather(location)
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