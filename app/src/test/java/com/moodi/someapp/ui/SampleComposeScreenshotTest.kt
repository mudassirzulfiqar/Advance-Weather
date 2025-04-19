package com.moodi.someapp.ui

import app.cash.paparazzi.Paparazzi
import com.moodi.someapp.MainScreen
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.viewmodel.WeatherUIState
import org.junit.Rule
import org.junit.Test

class SampleComposeScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun `verify screen with success view`() {
        paparazzi.snapshot {
            MainScreen(
                state = WeatherUIState(
                    loading = false,
                    WeatherAppData(
                        22.0, WeatherCondition.Snow, "Hilversum"
                    ),
                    error = null,
                )
            )
        }
    }

    @Test
    fun `verify screen for network Error`() {
        paparazzi.snapshot {
            MainScreen(
                state = WeatherUIState(
                    loading = false,
                    WeatherAppData(
                        22.0, WeatherCondition.Snow, "Hilversum"
                    ),
                    error = "Network Error",
                )
            )
        }
    }

    @Test
    fun `verify screen for empty data`() {
        paparazzi.snapshot {
            MainScreen(
                state = WeatherUIState(
                    loading = false,
                    WeatherAppData(
                        0.0, WeatherCondition.Snow, "Hilversum"
                    ),
                    error = "Network Error",
                )
            )
        }
    }
}
