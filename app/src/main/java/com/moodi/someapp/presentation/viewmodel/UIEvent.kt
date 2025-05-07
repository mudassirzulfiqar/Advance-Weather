package com.moodi.someapp.presentation.viewmodel

import com.moodi.someapp.domain.model.WeatherUnit

sealed class UIEvent {
    data class FetchWeather(
        val lat: Double,
        val lng: Double,
        val unit: WeatherUnit,
    ) : UIEvent()

    data class LocationResultFailure(val message: String) : UIEvent()
}
