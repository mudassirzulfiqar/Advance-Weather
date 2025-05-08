package com.moodi.someapp.data.remote.api

import com.moodi.someapp.core.common.Result
import com.moodi.someapp.data.remote.client.RemoteClient
import com.moodi.someapp.data.remote.dto.WeatherDto
import com.moodi.someapp.data.remote.dto.WeatherRequest

interface ApiService {
    suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto>
}


class WeatherApiService(private val remoteApiClient: RemoteClient) : ApiService {
    override suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto> {
        return remoteApiClient.fetchWeather(
            weatherRequest.lng, weatherRequest.lat, weatherRequest.unit
        );
    }
}