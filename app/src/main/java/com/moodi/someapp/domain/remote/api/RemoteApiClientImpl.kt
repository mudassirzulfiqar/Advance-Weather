package com.moodi.someapp.domain.remote.api

import com.moodi.someapp.core.common.Result
import com.moodi.someapp.data.remote.dto.ErrorDto
import com.moodi.someapp.data.remote.dto.WeatherDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class BadRequestException(message: String) : Exception(message)

class RemoteApiClientImpl(val client: HttpClient) : RemoteApiClient {

    /*
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
    */

    override suspend fun fetchWeather(
        lat: Double,
        lng: Double,
        unit: String,
    ): Result<WeatherDto> {
        return executeApi<WeatherDto>(
            apiEndpoint = ApiEndpoint.WEATHER,
            requestBuilder = {
                parameter("lat", lat)
                parameter("lon", lng)
                parameter("units", unit)
            },
        )
    }

    suspend inline fun <reified T> executeApi(
        apiEndpoint: ApiEndpoint,
        requestBuilder: HttpRequestBuilder.() -> Unit,
    ): Result<T> {
        try {
            val result = client.get(apiEndpoint.path) {
                requestBuilder()
            }

            if (result.status.isSuccess()) {
                return Result.Success(result.body<T>())
            } else {
                val errorDto = result.body<ErrorDto>()
                return Result.Failure(
                    data = result.body<ErrorDto>().message,
                    error = BadRequestException(errorDto.message)
                )
            }

        } catch (e: Exception) {
            return Result.Failure(data = e.message.toString(), error = e)
        }
    }
}
