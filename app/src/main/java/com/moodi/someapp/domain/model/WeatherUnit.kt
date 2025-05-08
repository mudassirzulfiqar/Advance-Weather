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

class WeatherApp(
    val temperature: Double,
    val condition: List<WeatherCondition>,
    val locationName: String
)