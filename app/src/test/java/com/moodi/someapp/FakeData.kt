package com.moodi.someapp

import com.moodi.someapp.domain.remote.dto.Main
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.domain.remote.dto.WeatherList


val SampleWeatherDTO = WeatherDto(
    main = Main(
        0.00
    ), weather = listOf(WeatherList(main = "Sunny")), name = "Hilversum"
)