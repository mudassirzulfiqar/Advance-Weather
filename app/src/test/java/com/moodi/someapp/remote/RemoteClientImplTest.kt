package com.moodi.someapp.remote

import com.moodi.someapp.Result
import com.moodi.someapp.location.AppLocation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test


class RemoteClientImplTest {

    //language=JSON
    val errorResponseForInValidLocation = """{
      "cod": "400",
      "message": "wrong longitude"
      }"""

    val successResponseForValidLocation = """{
      "main": {
        "temp": 25.0
      }
    }"""

    val mockEngine = MockEngine { request ->
        val path = request.url.encodedPath // e.g., /data/2.5/weather
        val lat = request.url.parameters["lat"]
        val lon = request.url.parameters["lon"]

        // 1. Validate endpoint path
        if (path != "/data/2.5/weather") {
            return@MockEngine respond(
                content = """{"error": "Invalid endpoint"}""",
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // 2. Check if lat or lon is missing
        if (lat == null || lon == null) {
            return@MockEngine respond(
                content = errorResponseForInValidLocation, // your mock error JSON
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // 3. Try parsing lat/lon to Double
        val latDouble = lat.toDoubleOrNull()
        val lonDouble = lon.toDoubleOrNull()

        if (latDouble == null || lonDouble == null) {
            return@MockEngine respond(
                content = """{"error": "Invalid lat/lon format"}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // 4. Check if lat/lon are within valid ranges
        if (latDouble !in -90.0..90.0 || lonDouble !in -180.0..180.0) {
            return@MockEngine respond(
                content = errorResponseForInValidLocation,
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // 5. If all is valid, return success
        return@MockEngine respond(
            content = successResponseForValidLocation, // your mock success JSON
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
    lateinit var remoteClient: RemoteClient

    @Before
    fun setup() {

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        remoteClient = RemoteClientImpl(client)
    }


    @Test
    fun `given location return weather in remote api`() = runTest {

        // Call the fetchWeather method
        val result = remoteClient.fetchWeather(
            AppLocation(
                latitude = 37.7749, longitude = -122.4194
            )
        )
        // Verify the result
        assert(result is Result.Success)
        assert((result as Result.Success).data.main.temp == 25.0)
    }

    /**
     * {
     * 	"cod": "400",
     * 	"message": "wrong latitude"
     * }
     */
    @Test
    fun `given unsupported location return error code `() = runTest {

        // Call the fetchWeather method
        val result = remoteClient.fetchWeather(
            AppLocation(
                latitude = 200.0, longitude = 200.0
            )
        )
        // Verify the exceptions
        assert(result is Result.Failure)
    }

}