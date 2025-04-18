package com.moodi.someapp.repository

import app.cash.turbine.test
import com.moodi.someapp.SampleWeatherDTO
import com.moodi.someapp.data.repository.WeatherRepositoryImpl
import com.moodi.someapp.data.util.Resource
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.api.WeatherApiClient
import com.moodi.someapp.domain.remote.client.BadRequestException
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    val weatherApiClient = mockk<WeatherApiClient>()

    lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        weatherRepository = WeatherRepositoryImpl(weatherApiClient)
    }

    @Test
    fun `on lat lng return success weather`() = runTest {

        coEvery { weatherApiClient.getWeather(any()) } returns Result.Success(
            SampleWeatherDTO
        )

        weatherRepository.getWeather(
            lat = 0.00,
            lng = 0.00,
            unit = WeatherUnit.METRIC
        ).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val successResult = awaitItem()

            assert((successResult as Resource.Success).data?.temperature == 0.0)
            awaitComplete()
        }
    }

    @Test
    fun `on lat lng return error weather`() = runTest {

        coEvery { weatherApiClient.getWeather(any()) } returns Result.Failure(
            "",
            Exception("Network Error"),
        )

        weatherRepository.getWeather(0.00, 0.00, WeatherUnit.METRIC).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val errorResult = awaitItem()

            assert((errorResult as Resource.Error).message == "Something Went Wrong")
            awaitComplete()
        }
    }

    @Test
    fun `on lat lng return remote error weather`() = runTest {

        coEvery { weatherApiClient.getWeather(any()) } returns Result.Failure(
            "", BadRequestException("")
        )

        weatherRepository.getWeather(0.00, 0.00, WeatherUnit.METRIC).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val errorResult = awaitItem()

            assert((errorResult is Resource.Error))
            awaitComplete()
        }
    }
}
