package com.moodi.someapp.domain.remote.client

import com.moodi.someapp.util.Result
import com.moodi.someapp.domain.remote.dto.ErrorDto
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.core.location.AppLocation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class BadRequestException(message: String) : Exception(message)

class RemoteClientImpl(val client: HttpClient) : RemoteClient {

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

    override suspend fun fetchWeather(location: AppLocation): Result<WeatherDto> {
        return executeApi<WeatherDto>(
            apiEndpoint = ApiEndpoint.WEATHER,
            requestBuilder = {
                parameter("lat", location.latitude)
                parameter("lon", location.longitude)
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
