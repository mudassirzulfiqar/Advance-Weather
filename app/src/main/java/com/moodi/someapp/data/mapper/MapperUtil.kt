package com.moodi.someapp.data.mapper

import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.data.remote.dto.*

fun List<WeatherList>.mapToCondition() = if (this.isEmpty()) {
    WeatherCondition.Other
} else {
    when (this[0].main) {
        "Rain" -> WeatherCondition.Rain
        "Sunny" -> WeatherCondition.Sunny
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
