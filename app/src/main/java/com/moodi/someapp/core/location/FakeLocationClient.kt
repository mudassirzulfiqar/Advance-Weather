package com.moodi.someapp.core.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocationClient : LocationClient {
    override suspend fun getCurrentLocation(): Flow<LocationResult> = flow {
        emit(LocationResult.Success(latitude = 0.0, longitude = 0.0))
    }
}
