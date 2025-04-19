package com.moodi.someapp.location

import com.moodi.someapp.core.location.LocationClient
import com.moodi.someapp.core.location.LocationResult
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AppLocationClientTest {

    @Test
    fun `return any user location`() = runTest {
        val locationClient = FakeLocationClient()
        val userLocation = locationClient.getCurrentLocation()

        // check if user location produces a valid location

        assert(userLocation is LocationResult.Success)


        when (userLocation) {
            is LocationResult.Success -> {
                assert(userLocation.latitude in -90.0..90.0)
                assert(userLocation.longitude in -180.0..180.0)
            }

            is LocationResult.Failure -> {
                error("Expected Success but got Error")
            }
        }

    }

    class FakeLocationClient : LocationClient {
        override suspend fun getCurrentLocation(): LocationResult {
            return LocationResult.Success(latitude = 0.0, longitude = 0.0)
        }
    }

}