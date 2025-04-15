package com.moodi.someapp

import com.moodi.someapp.core.location.AppLocation
import com.moodi.someapp.domain.remote.client.RemoteClient
import com.moodi.someapp.domain.remote.api.WeatherApiClient
import com.moodi.someapp.domain.remote.dto.Main
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.domain.remote.dto.WeatherRequest
import com.moodi.someapp.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherApiClientTest {

    private val remoteClient = mockk<RemoteClient>()


    @Test
    fun `return weather data`() = runTest {

        val result = Result.Success<WeatherDto>(
            WeatherDto(
                main = Main(
                    temp = 25.0,
                )
            )
        )

        coEvery { remoteClient.fetchWeather(any()) } returns result
        val apiClient = WeatherApiClient(remoteClient)
        val weatherRequest = WeatherRequest(
            AppLocation(
                latitude = 37.7749,
                longitude = -122.4194,
            ),
        )
        val weatherResponse = apiClient.getWeather(weatherRequest)
        assertTrue(weatherResponse is Result.Success)
        assertEquals(25.0, (weatherResponse as Result.Success).data.main.temp, 0.1)
    }

    @Test
    fun `return weather data for different location`() = runTest {

        val result = Result.Success<WeatherDto>(
            WeatherDto(
                main = Main(
                    temp = 15.0,
                )
            )
        )

        coEvery { remoteClient.fetchWeather(any()) } returns result
        val apiClient = WeatherApiClient(remoteClient)
        val weatherRequest = WeatherRequest(
            AppLocation(
                latitude = 40.7128,
                longitude = -74.0060
            ),
        )
        val weatherResponse = apiClient.getWeather(weatherRequest)
        assertTrue(weatherResponse is Result.Success)
        assert((weatherResponse as Result.Success).data.main.temp in 0.0..50.0)
    }
}