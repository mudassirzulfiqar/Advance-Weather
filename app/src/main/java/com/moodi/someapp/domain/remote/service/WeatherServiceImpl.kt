package com.moodi.someapp.domain.remote.service

import com.moodi.someapp.core.common.Result
import com.moodi.someapp.data.remote.dto.WeatherDto
import com.moodi.someapp.data.remote.dto.WeatherRequest
import com.moodi.someapp.domain.remote.api.RemoteApiClient


interface WeatherService {
    suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto>
}


class WeatherServiceImpl(private val remoteApiClient: RemoteApiClient) : WeatherService {
    override suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherDto> {
        return remoteApiClient.fetchWeather(
            weatherRequest.lng,
            weatherRequest.lat,
            weatherRequest.unit
        );
    }
}