package com.moodi.someapp.location

import kotlinx.coroutines.test.runTest
import org.junit.Test

class AppLocationClientTest {

    @Test
    fun `return any user location`() = runTest {
        val locationClient = FakeLocationClient()
        val userLocation = locationClient.getCurrentLocation()

        // check if user location produces a valid location
        assert(userLocation.latitude in -90.0..90.0)
        assert(userLocation.longitude in -180.0..180.0)
    }

    class FakeLocationClient : LocationClient {
        override suspend fun getCurrentLocation(): AppLocation {
            return AppLocation(latitude = 0.0, longitude = 0.0)
        }
    }

}