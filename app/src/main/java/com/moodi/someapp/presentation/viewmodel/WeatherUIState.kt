package com.moodi.someapp.presentation.viewmodel

import com.moodi.someapp.domain.model.WeatherApp
import com.moodi.someapp.domain.model.WeatherUnit

data class WeatherUIState(
    val loading: Boolean = false,
    val weatherData: WeatherApp? = null,
    val unit: WeatherUnit = WeatherUnit.METRIC,
    val error: String? = null
)
