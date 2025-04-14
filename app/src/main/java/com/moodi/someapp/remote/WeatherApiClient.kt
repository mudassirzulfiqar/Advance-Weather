package com.moodi.someapp.remote

import com.moodi.someapp.Result
import com.moodi.someapp.location.AppLocation
import kotlinx.serialization.Serializable

enum class WeatherUnit(val value: String, val symbol: String) {
    METRIC("metric", "°C"),
    IMPERIAL("imperial", "°F"),
    STANDARD("standard", "K")
}

@Serializable
data class ErrorResponse(
    val cod: String, val message: String
)

data class WeatherRequest(
    val location: AppLocation,
)

@Serializable
data class WeatherResponse(
    val main: Main
)

@Serializable
data class Main(
    val temp: Double
)

interface ApiClient {
    suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherResponse>
}


class WeatherApiClient(private val remoteClient: RemoteClient) : ApiClient {
    override suspend fun getWeather(weatherRequest: WeatherRequest): Result<WeatherResponse> {
        return remoteClient.fetchWeather(weatherRequest.location);
    }
}