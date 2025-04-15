package com.moodi.someapp.data.mapper

import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.dto.WeatherDto

fun WeatherDto.mapToApp(): WeatherAppData {
    return WeatherAppData(
        temperature = this.main.temp,
        condition = WeatherCondition.Cloudy,
        unit = WeatherUnit.METRIC
    )
}