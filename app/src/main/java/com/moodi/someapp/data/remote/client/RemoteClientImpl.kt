package com.moodi.someapp.data.remote.client

import com.moodi.someapp.core.common.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import com.moodi.someapp.data.remote.dto.*
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class BadRequestException(message: String) : Exception(message)

class RemoteClientImpl(val client: HttpClient) : RemoteClient {

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
