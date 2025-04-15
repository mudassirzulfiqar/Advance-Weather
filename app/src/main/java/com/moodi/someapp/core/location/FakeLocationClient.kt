package com.moodi.someapp.core.location

class FakeLocationClient : LocationClient {
    override suspend fun getCurrentLocation(): AppLocation {
        return AppLocation(latitude = 0.0, longitude = 0.0)
    }
}
