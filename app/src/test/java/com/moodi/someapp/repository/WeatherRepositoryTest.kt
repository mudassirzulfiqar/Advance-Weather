package com.moodi.someapp.repository

import app.cash.turbine.test
import com.moodi.someapp.SampleWeatherDTO
import com.moodi.someapp.core.common.Resource
import com.moodi.someapp.core.common.Result
import com.moodi.someapp.data.repository.WeatherRepositoryImpl
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.api.BadRequestException
import com.moodi.someapp.domain.remote.service.WeatherServiceImpl
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.provideFakeWeatherDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    val weatherServiceImpl = mockk<WeatherServiceImpl>()

    lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        weatherRepository = WeatherRepositoryImpl(weatherServiceImpl)
    }

    @Test
    fun `on lat lng return success weather`() = runTest {

        coEvery { weatherServiceImpl.getWeather(any()) } returns Result.Success(
            SampleWeatherDTO
        )

        weatherRepository.getWeather(
            lat = 0.00, lng = 0.00, unit = WeatherUnit.METRIC
        ).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val successResult = awaitItem()

            assert((successResult as Resource.Success).data?.temperature == 0.0)
            assert(successResult.data?.locationName == "Hilversum")
            assert(successResult.data?.condition == WeatherCondition.Sunny)
            awaitComplete()
        }
    }

    @Test
    fun `on lat lng return success weather with unknown Condition`() = runTest {

        coEvery { weatherServiceImpl.getWeather(any()) } returns Result.Success(
            provideFakeWeatherDto(0.0, "Any", "Hilversum")
        )

        weatherRepository.getWeather(
            lat = 0.00, lng = 0.00, unit = WeatherUnit.METRIC
        ).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val successResult = awaitItem()

            assert((successResult as Resource.Success).data?.temperature == 0.0)
            assert(successResult.data?.locationName == "Hilversum")
            assert(successResult.data?.condition == WeatherCondition.Other)
            awaitComplete()
        }
    }

    @Test
    fun `on lat lng return error weather`() = runTest {

        coEvery { weatherServiceImpl.getWeather(any()) } returns Result.Failure(
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

        coEvery { weatherServiceImpl.getWeather(any()) } returns Result.Failure(
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
