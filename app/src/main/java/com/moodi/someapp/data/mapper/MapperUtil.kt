package com.moodi.someapp.data.mapper

import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.domain.remote.dto.WeatherList

fun List<WeatherList>.mapToCondition() = if (this.isEmpty()) {
    WeatherCondition.NotAvailable
} else {
    when (this[0].main) {
        "Rain" -> WeatherCondition.Rain
        else -> {
            WeatherCondition.Other
        }
    }
}


fun WeatherDto.mapToApp(): WeatherAppData {
    return WeatherAppData(
        temperature = main.temp,
        condition = weather.mapToCondition(),
        locationName = name
    )
}
