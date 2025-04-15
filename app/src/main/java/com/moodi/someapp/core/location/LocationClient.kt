package com.moodi.someapp.core.location


interface LocationClient {
    suspend fun getCurrentLocation(): AppLocation
}


