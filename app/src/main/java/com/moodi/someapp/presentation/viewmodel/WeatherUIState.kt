package com.moodi.someapp.presentation.viewmodel

import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherUnit

data class WeatherUIState(
    val loading: Boolean = false,
    val weatherData: WeatherAppData? = null,
    val unit: WeatherUnit = WeatherUnit.METRIC,
    val error: String? = null
)
