package com.moodi.someapp.domain.model

enum class WeatherUnit(val value: String, val symbol: String) {
    METRIC("metric", "°C"),
    IMPERIAL("imperial", "°F"),
    STANDARD("standard", "K")
}

enum class WeatherCondition(val condition: String) {
    Sunny("Sunny"),
    Snow("Snow"),
    Rain("Rainy"),
    Cloudy("Cloudy"),
    Other("Not Supported")
}

class WeatherAppData(
    val temperature: Double,
    val condition: WeatherCondition,
    val locationName: String
)