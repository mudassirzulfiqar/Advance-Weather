package com.moodi.someapp.location

import app.cash.turbine.test
import com.moodi.someapp.core.location.LocationClient
import com.moodi.someapp.core.location.LocationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AppLocationClientTest {

    @Test
    fun `return any user location`() = runTest {
        val locationClient = FakeLocationClientSuccess()
        val userLocation = locationClient.getCurrentLocation()

        // check if user location produces a valid location

        userLocation.test {

            val result = awaitItem()

            assert(result is LocationResult.Success)

            when (result) {
                is LocationResult.Success -> {
                    assert(result.latitude in -90.0..90.0)
                    assert(result.longitude in -180.0..180.0)
                }

                is LocationResult.Failure -> {
                    error("Expected Success but got Error")
                }
            }
            awaitComplete()
        }

    }

    @Test
    fun `return error when location is not available`() = runTest {
        val locationClient = FakeLocationClientError()
        val userLocation = locationClient.getCurrentLocation()

        userLocation.test {

            val result = awaitItem()

            assert(result is LocationResult.Failure)

            when (result) {
                is LocationResult.Success -> {
                    error("Expected Error but got Success")
                }

                is LocationResult.Failure -> {
                    assert(result.reason == "Unable to fetch location")
                }
            }
            awaitComplete()
        }

    }

    @Test
    fun `return exception when location is not available`() = runTest {
        val locationClient = FakeLocationClientException()
        val userLocation = locationClient.getCurrentLocation()

        userLocation.test {
            val error = awaitError()
            assert(error is Exception)
        }

    }


    class FakeLocationClientSuccess : LocationClient {
        override suspend fun getCurrentLocation(): Flow<LocationResult> {
            return flow {
                emit(LocationResult.Success(latitude = 0.0, longitude = 0.0))
            }
        }
    }

    class FakeLocationClientError : LocationClient {
        override suspend fun getCurrentLocation(): Flow<LocationResult> {
            return flow {
                emit(LocationResult.Failure(reason = "Unable to fetch location"))
            }
        }
    }

    class FakeLocationClientException : LocationClient {
        override suspend fun getCurrentLocation(): Flow<LocationResult> {
            return flow {
                throw Exception("Unable to fetch location")
            }
        }
    }

}