package com.moodi.someapp.domain.repository

import com.moodi.someapp.core.common.Resource
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherUnit
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(lat: Double, lng: Double, unit: WeatherUnit): Flow<Resource<WeatherAppData>>
}