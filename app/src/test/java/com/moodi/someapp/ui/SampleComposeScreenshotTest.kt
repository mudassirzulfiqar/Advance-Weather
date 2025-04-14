package com.moodi.someapp.ui

import app.cash.paparazzi.Paparazzi
import com.moodi.someapp.MainScreen
import com.moodi.someapp.remote.Main
import com.moodi.someapp.remote.WeatherResponse
import org.junit.Rule
import org.junit.Test

class SampleComposeScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun `verify screen with detail matching temperature and description`() {
        paparazzi.snapshot {
            MainScreen(
                showError = false,
                result = WeatherResponse(
                    main = Main(
                        temp = 22.0,
                    )
                )
            )
        }
    }

    @Test
    fun `verify screen for network Error`() {
        paparazzi.snapshot {
            MainScreen(
                showError = true,
                result = WeatherResponse(
                    main = Main(
                        temp = 22.0,
                    )
                )
            )
        }
    }

    @Test
    fun `verify screen for empty data`() {
        paparazzi.snapshot {
            MainScreen(
                showError = false,
                result = WeatherResponse(
                    main = Main(
                        temp = 0.0,
                    )
                )
            )
        }
    }
}
