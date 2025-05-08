package com.moodi.someapp.data.mapper

import com.moodi.someapp.data.local.entity.WeatherEntity
import com.moodi.someapp.data.remote.dto.WeatherDto
import com.moodi.someapp.domain.model.WeatherApp
import com.moodi.someapp.domain.model.WeatherCondition

fun String.toWeatherCondition(): WeatherCondition {
    return when (this) {
        "Rain" -> WeatherCondition.Rain
        "Sunny" -> WeatherCondition.Sunny
        else -> WeatherCondition.Other
    }
}

//fun List<WeatherList>.toWeatherCondition() = this.map { it.main.toWeatherCondition() }
fun List<String>.toWeatherCondition() = this.map { it.toWeatherCondition() }

fun WeatherDto.toWeatherApp(): WeatherApp {
    return WeatherApp(
        temperature = main.temp,
        condition = weather.map { it.main.toWeatherCondition() },
        locationName = name
    )
}

fun WeatherEntity.toWeatherApp(): WeatherApp {
    return WeatherApp(
        temperature = temperature ?: 0.0,
        condition = listOf(this.condition?.toWeatherCondition() ?: WeatherCondition.Other),
        locationName = location ?: ""
    )
}

fun WeatherDto.toEntity(): WeatherEntity {
    return WeatherEntity(
        temperature = this.main.temp,
        location = this.name,
        condition = this.weather.map { it.main }.first(),
        timestamp = System.currentTimeMillis(),
    )
}
