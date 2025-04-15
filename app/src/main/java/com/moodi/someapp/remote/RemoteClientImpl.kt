package com.moodi.someapp.remote

import com.moodi.someapp.Result
import com.moodi.someapp.location.AppLocation
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

    override suspend fun fetchWeather(location: AppLocation): Result<WeatherResponse> {
        return executeApi<WeatherResponse>(
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
                val errorResponse = result.body<ErrorResponse>()
                return Result.Failure(
                    data = result.body<ErrorResponse>().message,
                    error = BadRequestException(errorResponse.message)
                )
            }

        } catch (e: Exception) {
            return Result.Failure(data = e.message.toString(), error = e)
        }
    }
}
