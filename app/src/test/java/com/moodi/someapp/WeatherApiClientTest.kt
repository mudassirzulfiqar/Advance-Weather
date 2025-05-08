package com.moodi.someapp

import com.moodi.someapp.core.common.Result
import com.moodi.someapp.data.remote.api.WeatherApiService
import com.moodi.someapp.data.remote.client.RemoteClient
import com.moodi.someapp.data.remote.dto.WeatherDto
import com.moodi.someapp.data.remote.dto.WeatherRequest
import com.moodi.someapp.domain.model.WeatherUnit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherApiClientTest {

    private val remoteApiClient = mockk<RemoteClient>()


    @Test
    fun `return weather data`() = runTest {

        val result = Result.Success<WeatherDto>(
            SampleWeatherDTO
        )

        coEvery {
            remoteApiClient.fetchWeather(
                any(),
                any(),
                WeatherUnit.METRIC.value
            )
        } returns result
        val apiClient = WeatherApiService(remoteApiClient)
        val weatherRequest = WeatherRequest(
            lat = 37.7749,
            lng = -122.4194,
            unit = WeatherUnit.METRIC.value
        )
        val weatherResponse = apiClient.getWeather(weatherRequest)
        assertTrue(weatherResponse is Result.Success)
        assertEquals(0.0, (weatherResponse as Result.Success).data.main.temp, 0.1)
    }

    @Test
    fun `return weather data for different location`() = runTest {

        val result = Result.Success<WeatherDto>(
            SampleWeatherDTO
        )

        coEvery {
            remoteApiClient.fetchWeather(
                any(),
                any(),
                WeatherUnit.METRIC.value
            )
        } returns result
        val apiClient = WeatherApiService(remoteApiClient)
        val weatherRequest = WeatherRequest(
            lat = 40.7128,
            lng = -74.0060,
            unit = WeatherUnit.METRIC.value

        )
        val weatherResponse = apiClient.getWeather(weatherRequest)
        assertTrue(weatherResponse is Result.Success)
        assert((weatherResponse as Result.Success).data.main.temp in 0.0..50.0)
    }
}