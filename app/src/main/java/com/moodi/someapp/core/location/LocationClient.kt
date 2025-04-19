package com.moodi.someapp.core.location

import kotlinx.coroutines.flow.Flow

sealed class LocationResult {
    data class Success(val latitude: Double, val longitude: Double) : LocationResult()
    data class Failure(val reason: String) : LocationResult()
}

interface LocationClient {
    suspend fun getCurrentLocation(): Flow<LocationResult>
}


