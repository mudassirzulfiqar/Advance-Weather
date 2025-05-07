package com.moodi.someapp.data.remote.dto

import com.moodi.someapp.domain.model.WeatherUnit
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val cod: String, val message: String
)

data class WeatherRequest(
    val lat: Double,
    val lng: Double,
    val unit: String = WeatherUnit.STANDARD.value,
)

@Serializable
data class WeatherDto(
    val main: Main,
    val weather: List<WeatherList>,
    val name: String,
)

@Serializable
data class WeatherList(val main: String)

@Serializable
data class Main(
    val temp: Double
)