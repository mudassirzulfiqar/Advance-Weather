package com.moodi.someapp.core.location

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LocationManager(
    private val locationModule: FusedLocationProviderClient,
    private val locationPermissionCheck: LocationPermissionCheck
) : LocationClient {

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    override suspend fun getCurrentLocation(): Flow<LocationResult> = flow {

        try {
            if (!locationPermissionCheck.checkCoursePermission()) {
                emit(LocationResult.Failure("Missing Location Permission"))
            }

            val cancellationTokenSource = CancellationTokenSource()
            val currentLocationTask = locationModule.getCurrentLocation(
                PRIORITY_BALANCED_POWER_ACCURACY, cancellationTokenSource.token
            )
            val currentLocation = currentLocationTask.await(cancellationTokenSource)

            emit(
                LocationResult.Success(
                    latitude = currentLocation.latitude, longitude = currentLocation.longitude
                )
            )
        } catch (e: Exception) {
            emit(LocationResult.Failure("Unable to fetch location"))
        }
    }
}
