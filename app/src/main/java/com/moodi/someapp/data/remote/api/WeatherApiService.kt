package com.moodi.someapp.data.remote.api

import com.moodi.someapp.core.common.Result
import com.moodi.someapp.domain.remote.api.RemoteApiClient
import com.moodi.someapp.data.remote.dto.*


interface ApiService {
    suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto>
}


class WeatherApiService(private val remoteApiClient: RemoteApiClient) : ApiService {
    override suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto> {
        return remoteApiClient.fetchWeather(
            weatherRequest.lng, weatherRequest.lat, weatherRequest.unit
        );
    }
}