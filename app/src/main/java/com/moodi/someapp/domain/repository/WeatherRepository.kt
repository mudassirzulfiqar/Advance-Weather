package com.moodi.someapp.domain.repository

import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(lat: Double, lng: Double): Flow<Resource<WeatherAppData>>
}