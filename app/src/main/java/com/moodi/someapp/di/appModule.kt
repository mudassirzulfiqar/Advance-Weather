package com.moodi.someapp.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.core.location.LocationPermissionCheck
import com.moodi.someapp.data.repository.WeatherRepositoryImpl
import com.moodi.someapp.domain.remote.api.ApiClient
import com.moodi.someapp.domain.remote.api.WeatherApiClient
import com.moodi.someapp.domain.remote.client.RemoteClient
import com.moodi.someapp.domain.remote.client.RemoteClientImpl
import com.moodi.someapp.domain.remote.client.client
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.viewmodel.WeatherViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single<LocationPermissionCheck> { LocationPermissionCheck(get<Context>()) }
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<LocationManager> { LocationManager(get(), get()) }
    single<RemoteClient> { RemoteClientImpl(client) }
    single<ApiClient> { WeatherApiClient(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    viewModelOf(::WeatherViewModel)
}