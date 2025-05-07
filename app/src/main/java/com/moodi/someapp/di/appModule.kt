package com.moodi.someapp.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.core.location.LocationPermissionCheck
import com.moodi.someapp.data.repository.WeatherRepositoryImpl
import com.moodi.someapp.domain.remote.service.WeatherService
import com.moodi.someapp.domain.remote.service.WeatherServiceImpl
import com.moodi.someapp.domain.remote.api.RemoteApiClient
import com.moodi.someapp.domain.remote.api.RemoteApiClientImpl
import com.moodi.someapp.domain.remote.api.client
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.presentation.viewmodel.WeatherViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single<LocationPermissionCheck> { LocationPermissionCheck(get<Context>()) }
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<LocationManager> { LocationManager(get(), get()) }
    single<RemoteApiClient> { RemoteApiClientImpl(client) }
    single<WeatherService> { WeatherServiceImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    viewModelOf(::WeatherViewModel)
}