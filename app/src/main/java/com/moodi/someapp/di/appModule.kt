package com.moodi.someapp.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.core.location.LocationPermissionCheck
import com.moodi.someapp.data.local.dao.WeatherDao
import com.moodi.someapp.data.local.provideWeatherDatabase
import com.moodi.someapp.data.remote.api.ApiService
import com.moodi.someapp.data.remote.api.WeatherApiService
import com.moodi.someapp.data.remote.client.RemoteClient
import com.moodi.someapp.data.remote.client.RemoteClientImpl
import com.moodi.someapp.data.remote.client.client
import com.moodi.someapp.data.repository.WeatherRepositoryImpl
import com.moodi.someapp.domain.repository.WeatherRepository
import com.moodi.someapp.presentation.viewmodel.WeatherViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single<LocationPermissionCheck> { LocationPermissionCheck(get<Context>()) }
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<LocationManager> { LocationManager(get(), get()) }
    single<RemoteClient> { RemoteClientImpl(client) }
    single<ApiService> { WeatherApiService(get()) }
    single<WeatherDao> { provideWeatherDatabase(get<Context>()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    viewModelOf(::WeatherViewModel)
}