package com.moodi.someapp.repository

import app.cash.turbine.test
import com.moodi.someapp.Result
import com.moodi.someapp.remote.ErrorResponse
import com.moodi.someapp.remote.Main
import com.moodi.someapp.remote.RemoteClient
import com.moodi.someapp.remote.WeatherApiClient
import com.moodi.someapp.remote.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    val remoteClient = mockk<RemoteClient>()

    lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        weatherRepository = WeatherRepositoryImpl(remoteClient)
    }

    @Test
    fun `on lat lng return success weather`() = runTest {

        coEvery { remoteClient.fetchWeather(any()) } returns Result.Success(
            WeatherResponse(
                Main(
                    22.0
                )
            )
        )

        weatherRepository.getWeather(0.00, 0.00).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val successResult = awaitItem()

            assert((successResult as Resource.Success).data?.temperature == 22.0)
            awaitComplete()
        }
    }

    @Test
    fun `on lat lng return error weather`() = runTest {

        coEvery { remoteClient.fetchWeather(any()) } returns Result.Error(
            Exception("Network Error"),
            null
        )

        weatherRepository.getWeather(0.00, 0.00).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val errorResult = awaitItem()

            assert((errorResult as Resource.Error).message == "Something Went Wrong")
            awaitComplete()
        }
    }

    @Test
    fun `on lat lng return remote error weather`() = runTest {

        coEvery { remoteClient.fetchWeather(any()) } returns Result.RemoteError(
            ErrorResponse(
                "400",
                "wrong latitude"
            )
        )

        weatherRepository.getWeather(0.00, 0.00).test {
            val result = awaitItem()

            assert(result is Resource.Loading)

            val errorResult = awaitItem()

            assert((errorResult as Resource.Error).message == "Network Problem")
            awaitComplete()
        }
    }
}
