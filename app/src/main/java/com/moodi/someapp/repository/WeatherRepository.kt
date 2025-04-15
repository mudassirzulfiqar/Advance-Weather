package com.moodi.someapp.repository

import com.moodi.someapp.Result
import com.moodi.someapp.location.AppLocation
import com.moodi.someapp.remote.RemoteClient
import com.moodi.someapp.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


fun WeatherResponse.mapToApp(): WeatherAppData {
    return WeatherAppData(
        temperature = this.main.temp,
        condition = WeatherCondition.Cloudy,
        unit = WeatherUnit.METRIC
    )
}


sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}


enum class WeatherUnit(val value: String, val symbol: String) {
    METRIC("metric", "°C"),
    IMPERIAL("imperial", "°F"),
    STANDARD("standard", "K")
}

enum class WeatherCondition(val condition: String) {
    Sunny("Sunny"), Snow("Snow"), Rain("Rainy"), Cloudy("Cloudy")
}

class WeatherAppData(
    val temperature: Double, val condition: WeatherCondition, val unit: WeatherUnit
)

interface WeatherRepository {
    fun getWeather(lat: Double, lng: Double): Flow<Resource<WeatherAppData>>
}

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