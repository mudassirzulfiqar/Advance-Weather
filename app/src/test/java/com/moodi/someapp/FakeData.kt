package com.moodi.someapp

import com.moodi.someapp.domain.remote.dto.Main
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.domain.remote.dto.WeatherList


fun provideFakeWeatherDto(temperature: Double, condition: String, location: String): WeatherDto {
    return WeatherDto(
        main = Main(temperature),
        weather = listOf(WeatherList(main = condition)),
        name = location
    )
}

val SampleWeatherDTO = WeatherDto(
    main = Main(
        0.00
    ), weather = listOf(WeatherList(main = "Sunny")), name = "Hilversum"
)