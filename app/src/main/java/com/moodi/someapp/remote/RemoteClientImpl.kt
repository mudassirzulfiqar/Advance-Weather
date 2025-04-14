package com.moodi.someapp.remote

import com.moodi.someapp.Result
import com.moodi.someapp.location.AppLocation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class RemoteClientImpl(val client: HttpClient) : RemoteClient {

    override suspend fun fetchWeather(location: AppLocation): Result<WeatherResponse> {
        return try {
            val result = client.get(ApiEndpoint.WEATHER.path) {
                parameter("lat", location.latitude)
                parameter("lon", location.longitude)
            }

            return if (result.status.isSuccess()) {
                Result.Success(result.body<WeatherResponse>())
            } else {
                Result.RemoteError(result.body<ErrorResponse>())
            }
        } catch (e: Exception) {
            return Result.Error(e, null)
        }
    }
}
