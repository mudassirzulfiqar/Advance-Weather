package com.moodi.someapp.domain.remote.dto

import com.moodi.someapp.core.location.AppLocation
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val cod: String, val message: String
)

data class WeatherRequest(
    val location: AppLocation,
)

@Serializable
data class WeatherDto(
    val main: Main
)

@Serializable
data class Main(
    val temp: Double
)