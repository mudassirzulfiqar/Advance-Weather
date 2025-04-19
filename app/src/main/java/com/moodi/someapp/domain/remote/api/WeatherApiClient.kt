package com.moodi.someapp.domain.remote.api

import com.moodi.someapp.util.Result
import com.moodi.someapp.domain.remote.client.RemoteClient
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.domain.remote.dto.WeatherRequest


interface ApiClient {
    suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto>
}


class WeatherApiClient(private val remoteClient: RemoteClient) : ApiClient {
    override suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto> {
        return remoteClient.fetchWeather(
            weatherRequest.lng,
            weatherRequest.lat,
            weatherRequest.unit
        );
    }
}