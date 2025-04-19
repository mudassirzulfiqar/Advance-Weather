package com.moodi.someapp.core.location

sealed class LocationResult {
    data class Success(val latitude: Double, val longitude: Double) : LocationResult()
    data class Failure(val reason: String) : LocationResult()
}

interface LocationClient {
    suspend fun getCurrentLocation(): LocationResult
}


