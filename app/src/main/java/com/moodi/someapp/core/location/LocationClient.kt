package com.moodi.someapp.core.location

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await

data class AppLocation(val latitude: Double, val longitude: Double)

interface LocationClient {
    suspend fun getCurrentLocation(): AppLocation
}

class FakeLocationClient : LocationClient {
    override suspend fun getCurrentLocation(): AppLocation {
        return AppLocation(latitude = 0.0, longitude = 0.0)
    }
}

class LocationManager(val locationModule: FusedLocationProviderClient) : LocationClient {

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation(): AppLocation {
        val cancellationTokenSource = CancellationTokenSource()
        val currentLocationTask =
            locationModule.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
        val currentLocation = currentLocationTask.await(cancellationTokenSource)

        return AppLocation(
            latitude = currentLocation.latitude, longitude = currentLocation.longitude
        )
    }
}
