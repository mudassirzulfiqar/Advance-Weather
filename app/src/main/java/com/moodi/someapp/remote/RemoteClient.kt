package com.moodi.someapp.remote

import com.moodi.someapp.BuildConfig
import com.moodi.someapp.Result
import com.moodi.someapp.location.AppLocation
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        level = LogLevel.ALL
    }
    install(DefaultRequest) {
        url {
            protocol = io.ktor.http.URLProtocol.HTTPS
            host = "api.openweathermap.org"
            parameters.append(
                "APPID", BuildConfig.OPEN_WEATHER_API_KEY
            )
        }
    }

}

enum class ApiEndpoint(val path: String) {
    WEATHER("data/2.5/weather");
}

interface RemoteClient {
    suspend fun fetchWeather(location: AppLocation): Result<WeatherResponse>
}


