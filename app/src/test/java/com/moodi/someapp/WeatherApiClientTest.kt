package com.moodi.someapp

import com.moodi.someapp.location.AppLocation
import com.moodi.someapp.remote.Main
import com.moodi.someapp.remote.RemoteClient
import com.moodi.someapp.remote.WeatherApiClient
import com.moodi.someapp.remote.WeatherRequest
import com.moodi.someapp.remote.WeatherResponse
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

        val result = Result.Success<WeatherResponse>(
            WeatherResponse(
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
        assertEquals(25.0, weatherResponse.result!!.main.temp, 0.1)
    }

    @Test
    fun `return weather data for different location`() = runTest {

        val result = Result.Success<WeatherResponse>(
            WeatherResponse(
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

        assert(weatherResponse.result!!.main.temp in 0.0..50.0)
    }
}